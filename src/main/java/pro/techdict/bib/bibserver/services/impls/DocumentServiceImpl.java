package pro.techdict.bib.bibserver.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPES;
import pro.techdict.bib.bibserver.daos.*;
import pro.techdict.bib.bibserver.dtos.DocShowInListItemDto;
import pro.techdict.bib.bibserver.dtos.DocumentCommentDto;
import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.entities.*;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.models.CommentModel;
import pro.techdict.bib.bibserver.models.DocumentMetaModel;
import pro.techdict.bib.bibserver.services.DocumentService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

  final UserRepository userRepository;
  final DocumentRepository documentRepository;
  final UserActivityRepository userActivityRepository;
  final DocumentCommentRepository documentCommentRepository;
  final WikiRepository wikiRepository;

  public DocumentServiceImpl(
      UserRepository userRepository,
      DocumentRepository documentRepository,
      UserActivityRepository userActivityRepository,
      DocumentCommentRepository documentCommentRepository,
      WikiRepository wikiRepository) {
    this.userRepository = userRepository;
    this.documentRepository = documentRepository;
    this.userActivityRepository = userActivityRepository;
    this.documentCommentRepository = documentCommentRepository;
    this.wikiRepository = wikiRepository;
  }

  @Override
  public DocumentViewData initializeNewDocument(Long creatorId, Long wikiId) {
    Optional<UserAccount> creator = userRepository.findById(creatorId);
    if (creator.isPresent()) {
      Document newDocument = new Document();
      newDocument.setCreator(creator.get());
      ArrayList<UserAccount> collaborators = new ArrayList<>();
      collaborators.add(creator.get());
      newDocument.setCollaborators(collaborators);

      // 如果 wikiId 不为 null，则表示是知识库文档
      if (wikiId != null) {
        Optional<Wiki> wiki = wikiRepository.findById(wikiId);
        wiki.ifPresent(newDocument::setInWiki);
      }

      Document savedDoc = documentRepository.save(newDocument);

      // 给用户添加一条创建文档的动态信息
      UserActivity createDocActivity = new UserActivity();
      createDocActivity.setActivityType(USER_ACTIVITY_TYPES.CREATE_DOC);
      createDocActivity.setCreatedDoc(savedDoc);
      createDocActivity.setCreator(creator.get());
      userActivityRepository.save(createDocActivity);

      return DocumentViewData.fromEntity(savedDoc);
    } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
  }

  @Override
  public DocumentViewData getDocumentViewData(Long docId, Long userId) {
    Optional<Document> doc = documentRepository.findById(docId);
    return doc.map(document -> DocumentViewData.fromEntity(document).setIsThumbsUpedByUserId(userId)).orElse(null);
  }

  @Override
  public DocumentViewData setDocumentMeta(DocumentMetaModel metaModel) {
    Optional<Document> updatingDoc = documentRepository.findById(metaModel.getDocId());
    if (updatingDoc.isPresent()) {
      updatingDoc.get().setTitle(metaModel.getTitle());
      updatingDoc.get().setContentAbstract(metaModel.getContentAbstract());
      updatingDoc.get().setPublicSharing(metaModel.getPublicSharing());
      return DocumentViewData.fromEntity(documentRepository.save(updatingDoc.get()));
    }

    return null;
  }

  @Override
  public DocShowInListItemDto getRecentRelativeDocumentList(Long userId, int pageNum) {
    Optional<UserAccount> user = userRepository.findById(userId);
    if (user.isPresent()) {
      List<Document> createdDocs = user.get().getCreatedDocs();
      List<Document> collaborateDocs = user.get().getCollaborateDocs();
      List<Document> commentedDocs = user.get().getCreatedComments().stream().map(
          DocumentComment::getTarget
      ).collect(Collectors.toList());

      Set<Document> allRelativeDocs = new HashSet<>();
      allRelativeDocs.addAll(createdDocs);
      allRelativeDocs.addAll(collaborateDocs);
      allRelativeDocs.addAll(commentedDocs);
      List<Document> sortedList = allRelativeDocs.stream()
          .sorted(Comparator.comparing(Document::getUpdateTime).reversed()).collect(Collectors.toList());
      int totalPages = (int) Math.ceil(sortedList.size() / 10.0);
      int startIndex = pageNum * 10;
      int endIndex = startIndex + 10;
      if (endIndex > sortedList.size()) { // 若是最后一页则结束索引为集合的最后一个索引
        endIndex = sortedList.size();
      }

      return DocShowInListItemDto.fromEntities(
          userId,
          sortedList.subList(startIndex, endIndex),
          totalPages
      );
    }

    return null;
  }

  @Override
  public DocShowInListItemDto getThumbsUpedDocumentList(Long userId, int pageNum) {
    Pageable pageable = PageRequest.of(pageNum, 10);
    Optional<UserAccount> user = userRepository.findById(userId);
    if (user.isPresent()) {
      List<Document> likedDocs = user.get().getLikedDocs();
      Page<Document> documentsByPage = new PageImpl<>(
          likedDocs,
          pageable,
          likedDocs.size()
      );
      return DocShowInListItemDto.fromEntities(
          userId,
          documentsByPage.getContent(),
          documentsByPage.getTotalPages()
      );
    }

    return null;
  }

  @Override
  public Boolean thumbsUpDocument(Long userId, Long docId) {
    Optional<UserAccount> thumbsUpUser = userRepository.findById(userId);
    Optional<Document> doc = documentRepository.findById(docId);
    if (thumbsUpUser.isPresent() && doc.isPresent()) {
      List<UserAccount> thumbUpUsers = doc.get().getThumbUpUsers();
      if (thumbUpUsers == null) {
        thumbUpUsers = new ArrayList<>();
      }
      if (thumbUpUsers.stream().anyMatch(user -> user.getId().equals(userId))) {
        thumbUpUsers.removeIf(user -> user.getId().equals(userId));
      } else {
        thumbUpUsers.add(thumbsUpUser.get());
      }

      documentRepository.save(doc.get());

      return true;
    }

    return false;
  }

  @Override
  public DocumentCommentDto addCommentToDocument(CommentModel commentModel) {
    DocumentComment newComment = new DocumentComment();
    Optional<UserAccount> creator = userRepository.findById(commentModel.getCreatorId());
    Optional<Document> doc = documentRepository.findById(commentModel.getDocId());

    if (creator.isEmpty()) throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    if (doc.isEmpty()) throw new CustomException(CustomExceptionType.DOCUMENT_NOT_FOUND);

    newComment.setContent(commentModel.getContent());
    newComment.setCreator(creator.get());

    if (commentModel.getReplyToId() != null) {
      Optional<DocumentComment> replyTo = documentCommentRepository.findById(commentModel.getReplyToId());
      replyTo.ifPresent(newComment::setReplyTo);
    }

    newComment.setTarget(doc.get());
    DocumentComment savedComment = documentCommentRepository.save(newComment);

    doc.get().getComments().add(savedComment);
    documentRepository.save(doc.get());
    return DocumentCommentDto.fromEntity(savedComment);
  }

  @Override
  public DocumentViewData addDocumentCollaborator(Long docId, Long invitingUserId) {
    Optional<Document> doc = documentRepository.findById(docId);
    if (doc.isPresent()) {
      Optional<UserAccount> invitingUser = userRepository.findById(invitingUserId);
      if (invitingUser.isPresent()) {
        doc.get().getCollaborators().add(invitingUser.get());
        Document savedDoc = documentRepository.save(doc.get());
        return DocumentViewData.fromEntity(savedDoc);
      } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    } else throw new CustomException(CustomExceptionType.DOCUMENT_NOT_FOUND);
  }

  @Override
  public Boolean addJoinRequest(Long docId, Long userId) {
    Optional<Document> targetDoc = documentRepository.findById(docId);
    if (targetDoc.isPresent()) {
      Optional<UserAccount> sender = userRepository.findById(userId);
      if (sender.isPresent()) {
        targetDoc.get().getPendingRequests().add(sender.get());
        documentRepository.save(targetDoc.get());
        return true;
      } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    } else throw new CustomException(CustomExceptionType.DOCUMENT_NOT_FOUND);
  }

  @Override
  public Boolean passJoinRequest(Long docId, Long userId) {
    Optional<Document> targetDoc = documentRepository.findById(docId);
    if (targetDoc.isPresent()) {
      Optional<UserAccount> sender = userRepository.findById(userId);
      if (sender.isPresent()) {
        targetDoc.get().getPendingRequests().remove(sender.get());
        targetDoc.get().getCollaborators().add(sender.get());
        documentRepository.save(targetDoc.get());
        return true;
      } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    } else throw new CustomException(CustomExceptionType.DOCUMENT_NOT_FOUND);
  }
}
