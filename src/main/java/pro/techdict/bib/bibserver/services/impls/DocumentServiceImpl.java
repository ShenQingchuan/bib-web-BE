package pro.techdict.bib.bibserver.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
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
    Pageable pageable = PageRequest.of(pageNum, 10);
    Optional<UserAccount> user = userRepository.findById(userId);
    if (user.isPresent()) {
      var createdDocs = user.get().getCreatedDocs();
      var collaborateDocs = user.get().getCollaborateDocs();
      var commentedDocs = user.get().getCreatedComments().stream().map(
          DocumentComment::getTarget
      ).collect(Collectors.toList());

      var allRelativeDocs = new HashSet<Document>();
      allRelativeDocs.addAll(createdDocs);
      allRelativeDocs.addAll(collaborateDocs);
      allRelativeDocs.addAll(commentedDocs);
      Page<Document> documentsByPage = new PageImpl<>(
          new ArrayList<>(allRelativeDocs),
          pageable,
          allRelativeDocs.size()
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
    var newComment = new DocumentComment();
    var creator = userRepository.findById(commentModel.getCreatorId());
    var doc = documentRepository.findById(commentModel.getDocId());

    if (creator.isEmpty()) throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    if (doc.isEmpty()) throw new CustomException(CustomExceptionType.DOCUMENT_NOT_FOUND);

    newComment.setContent(commentModel.getContent());
    newComment.setCreator(creator.get());

    if (commentModel.getReplyToId() != null) {
      var replyTo = documentCommentRepository.findById(commentModel.getReplyToId());
      replyTo.ifPresent(newComment::setReplyTo);
    }

    newComment.setTarget(doc.get());
    var savedComment = documentCommentRepository.save(newComment);

    doc.get().getComments().add(savedComment);
    documentRepository.save(doc.get());
    return DocumentCommentDto.fromEntity(savedComment);
  }

}
