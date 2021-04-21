package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.entities.Organization;

public interface OrganizationService {

  Organization createNewOrganization(
    String name,
    String desc,
    String avatarURL,
    Long creatorUid
  );

}
