package pro.techdict.bib.bibserver.services.impls;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.daos.UserActivityRepository;
import pro.techdict.bib.bibserver.dtos.UserActivitiesOnePageDto;
import pro.techdict.bib.bibserver.dtos.UserActivityDto;
import pro.techdict.bib.bibserver.entities.UserActivity;
import pro.techdict.bib.bibserver.services.UserActivityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserActivityServiceImpl implements UserActivityService {

  final UserActivityRepository userActivityRepository;

  public UserActivityServiceImpl(UserActivityRepository userActivityRepository) {
    this.userActivityRepository = userActivityRepository;
  }

  @Override
  public UserActivitiesOnePageDto getUserActivities(String userName, int pageNum) {
    Pageable pageable = PageRequest.of(pageNum, 10, Sort.Direction.DESC, "createTime");
    Page<UserActivity> pageableActivities = userActivityRepository.fetchPageableActivities(userName, pageable);

    List<UserActivityDto> activities = pageableActivities.getContent().stream().map(
        UserActivityDto::fromEntity
    ).collect(Collectors.toList());
    UserActivitiesOnePageDto onePageDto = new UserActivitiesOnePageDto();
    onePageDto.setActivities(activities);
    onePageDto.setPageTotal(pageableActivities.getTotalPages());

    return onePageDto;
  }
}
