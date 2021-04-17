package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.DocumentViewData;

public interface DocumentService {

  DocumentViewData initializeNewDocument(Long creatorId);

}
