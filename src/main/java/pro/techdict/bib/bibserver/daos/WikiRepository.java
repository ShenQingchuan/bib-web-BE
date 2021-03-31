package pro.techdict.bib.bibserver.daos;

import org.springframework.data.repository.CrudRepository;
import pro.techdict.bib.bibserver.entities.Wiki;

public interface WikiRepository extends CrudRepository<Wiki, Long> { }
