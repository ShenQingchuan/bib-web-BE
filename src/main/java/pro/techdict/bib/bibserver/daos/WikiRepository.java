package pro.techdict.bib.bibserver.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.techdict.bib.bibserver.entities.Document;
import pro.techdict.bib.bibserver.entities.Wiki;

public interface WikiRepository extends BaseRepository<Wiki, Long> {

  @Query("select w.documents from Wiki w where w.id = :wikiId")
  Page<Document> fetchDocumentsById(@Param("wikiId") Long wikiId, Pageable pageable);

}
