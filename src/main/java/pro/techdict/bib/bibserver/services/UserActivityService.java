package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.PageDto;
import pro.techdict.bib.bibserver.dtos.UserActivityDto;

public interface UserActivityService {

  PageDto<UserActivityDto> getUserActivities(String userName, int pageNum);

}
