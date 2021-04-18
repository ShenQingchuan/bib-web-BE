package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.UserActivitiesOnePageDto;

public interface UserActivityService {

  UserActivitiesOnePageDto getUserActivities(String userName, int pageNum);

}
