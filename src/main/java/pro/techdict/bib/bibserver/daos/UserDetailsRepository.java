package pro.techdict.bib.bibserver.daos;

import org.springframework.data.repository.CrudRepository;
import pro.techdict.bib.bibserver.entities.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> { }
