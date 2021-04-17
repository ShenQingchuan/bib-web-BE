package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.models.DocumentMetaModel;

public interface DocumentService {

  DocumentViewData initializeNewDocument(Long creatorId);

  DocumentViewData getDocumentViewData(Long docId);

  DocumentViewData setDocumentMeta(DocumentMetaModel metaModel);

}
