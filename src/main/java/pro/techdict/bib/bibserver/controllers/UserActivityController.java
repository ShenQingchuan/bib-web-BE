package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.techdict.bib.bibserver.dtos.PageDto;
import pro.techdict.bib.bibserver.dtos.UserActivityDto;
import pro.techdict.bib.bibserver.services.UserActivityService;
import pro.techdict.bib.bibserver.utils.HttpResponse;

@RestController
@RequestMapping("/activity")
public class UserActivityController {

  final UserActivityService userActivityService;

  public UserActivityController(UserActivityService userActivityService) {
    this.userActivityService = userActivityService;
  }

  @GetMapping("/")
  public HttpResponse getUserActivitiesByPageNum(
      @RequestParam String userName,
      @RequestParam int pageNum
  ) {
    PageDto<UserActivityDto> userActivitiesOnePageDto = userActivityService.getUserActivities(userName, pageNum);
    return HttpResponse.success("获取用户动态成功！", userActivitiesOnePageDto);
  }

}
