package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.DocShowInListItemDto;
import pro.techdict.bib.bibserver.dtos.DocumentCommentDto;
import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.models.CommentModel;
import pro.techdict.bib.bibserver.models.DocumentMetaModel;

public interface DocumentService {

  DocumentViewData initializeNewDocument(Long creatorId, Long wikiId);

  DocumentViewData getDocumentViewData(Long docId, Long userId);

  DocumentViewData setDocumentMeta(DocumentMetaModel metaModel);

  DocShowInListItemDto getRecentRelativeDocumentList(Long userId, int pageNum);

  DocShowInListItemDto getThumbsUpedDocumentList(Long userId, int pageNum);

  Boolean thumbsUpDocument(Long userId, Long docId);

  DocumentCommentDto addCommentToDocument(CommentModel commentModel);

  DocumentViewData addDocumentCollaborator(Long docId, Long invitingUserId);

  Boolean addJoinRequest(Long docId, Long userId);

  Boolean passJoinRequest(Long docId, Long userId);

}
