package pro.techdict.bib.bibserver.daos;

import org.springframework.data.repository.CrudRepository;
import pro.techdict.bib.bibserver.entities.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> { }
