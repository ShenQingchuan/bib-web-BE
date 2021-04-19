package pro.techdict.bib.bibserver.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.techdict.bib.bibserver.entities.Document;

public interface DocumentRepository extends BaseRepository<Document, Long> {

  @Query("select doc from Document doc where doc.creator.id = :creatorId")
  Page<Document> fetchByCreatorId(@Param("creatorId") Long creatorId, Pageable pageable);
}
