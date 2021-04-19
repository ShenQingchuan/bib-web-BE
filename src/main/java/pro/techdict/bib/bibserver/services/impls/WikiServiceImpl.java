package pro.techdict.bib.bibserver.services.impls;

import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPES;
import pro.techdict.bib.bibserver.daos.UserActivityRepository;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.daos.WikiRepository;
import pro.techdict.bib.bibserver.dtos.WikiSimpleDto;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserActivity;
import pro.techdict.bib.bibserver.entities.Wiki;
import pro.techdict.bib.bibserver.models.CreateWikiFormModel;
import pro.techdict.bib.bibserver.services.WikiService;

import java.util.Optional;

@Service
public class WikiServiceImpl implements WikiService {

  final WikiRepository wikiRepository;
  final UserRepository userRepository;
  final UserActivityRepository userActivityRepository;

  public WikiServiceImpl(
      WikiRepository wikiRepository,
      UserRepository userRepository,
      UserActivityRepository userActivityRepository) {
    this.wikiRepository = wikiRepository;
    this.userRepository = userRepository;
    this.userActivityRepository = userActivityRepository;
  }

  @Override
  public WikiSimpleDto createNewWiki(CreateWikiFormModel formModel) {
    Optional<UserAccount> creator = userRepository.findById(formModel.getUserId());
    if (creator.isPresent()) {
      Wiki newWiki = new Wiki();
      newWiki.setName(formModel.getName());
      newWiki.setDescription(formModel.getDesc());
      newWiki.setIsPrivate(formModel.getScope() == 0);
      newWiki.setCreator(creator.get());
      Wiki savedWiki = wikiRepository.save(newWiki);

      // 生成新的用户动态 - 创建知识库
      UserActivity createWikiActivity = new UserActivity();
      createWikiActivity.setCreator(creator.get());
      createWikiActivity.setActivityType(USER_ACTIVITY_TYPES.CREATE_WIKI);
      createWikiActivity.setCreatedWiki(savedWiki);
      userActivityRepository.save(createWikiActivity);

      return WikiSimpleDto.fromEntity(savedWiki);
    }

    return null;
  }

}
