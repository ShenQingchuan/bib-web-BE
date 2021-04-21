package pro.techdict.bib.bibserver.services.impls;

import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPES;
import pro.techdict.bib.bibserver.daos.OrganizationRepository;
import pro.techdict.bib.bibserver.daos.UserActivityRepository;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserActivity;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.services.OrganizationService;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final UserRepository userRepository;
  private final UserActivityRepository userActivityRepository;
  private final OrganizationRepository organizationRepository;

  public OrganizationServiceImpl(
      UserRepository userRepository,
      UserActivityRepository userActivityRepository,
      OrganizationRepository organizationRepository
  ) {
    this.userRepository = userRepository;
    this.userActivityRepository = userActivityRepository;
    this.organizationRepository = organizationRepository;
  }

  @Override
  public Organization createNewOrganization(
      String name,
      String desc,
      String avatarURL,
      Long creatorUid
  ) {
    Organization org = new Organization();
    org.setName(name);
    org.setDescription(desc);
    org.setAvatarURL(avatarURL);
    org.setMemberList(new ArrayList<>());

    Optional<UserAccount> creator = userRepository.findById(creatorUid);
    if (creator.isEmpty()) {
      throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    }

    // 记录创建者
    org.setCreator(creator.get());

    // 创建者必须要加入成员列表内
    org.getMemberList().add(creator.get());

    Organization savedNewOrg = organizationRepository.save(org);

    UserActivity createOrgActivity = new UserActivity();
    createOrgActivity.setCreator(creator.get());
    createOrgActivity.setCreatedOrg(savedNewOrg);
    createOrgActivity.setActivityType(USER_ACTIVITY_TYPES.CREATE_ORG);
    userActivityRepository.save(createOrgActivity);

    return savedNewOrg;
  }
}
