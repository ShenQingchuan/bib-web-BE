package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.models.OrganizationScope;

public interface OrganizationService {

  Organization createNewOrganization(
    String name,
    String desc,
    OrganizationScope scope,
    String avatarURL,
    Long creatorUid
  );

}
