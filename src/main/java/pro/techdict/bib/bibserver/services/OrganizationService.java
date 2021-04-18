package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.beans.ORGANIZATION_SCOPE;

public interface OrganizationService {

  Organization createNewOrganization(
    String name,
    String desc,
    ORGANIZATION_SCOPE scope,
    String avatarURL,
    Long creatorUid
  );

}
