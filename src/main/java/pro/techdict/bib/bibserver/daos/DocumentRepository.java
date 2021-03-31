package pro.techdict.bib.bibserver.daos;

import org.springframework.data.repository.CrudRepository;
import pro.techdict.bib.bibserver.entities.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> { }
