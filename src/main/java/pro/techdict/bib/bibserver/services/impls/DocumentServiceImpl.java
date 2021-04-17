package pro.techdict.bib.bibserver.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.daos.DocumentRepository;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.entities.Document;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.services.DocumentService;

import java.util.Optional;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

  final UserRepository userRepository;
  final DocumentRepository documentRepository;

  public DocumentServiceImpl(
      UserRepository userRepository,
      DocumentRepository documentRepository
  ) {
    this.userRepository = userRepository;
    this.documentRepository = documentRepository;
  }

  @Override
  public DocumentViewData initializeNewDocument(Long creatorId) {
    Optional<UserAccount> creator = userRepository.findById(creatorId);
    if (creator.isPresent()) {
      Document newDocument = new Document();
      newDocument.setCreator(creator.get());
      return new DocumentViewData().fromEntity(documentRepository.save(newDocument));
    }

    return null;
  }

}
