package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPE;
import pro.techdict.bib.bibserver.entities.UserActivity;

import java.util.Date;

@Data
public class UserActivityDto {
  Date createTime;
  USER_ACTIVITY_TYPE activityType;
  Object activityData;

  public UserActivityDto fromEntity(UserActivity activityEntity) {
    this.createTime = activityEntity.getCreateTime();
    this.activityType = activityEntity.getActivityType();
    switch (this.activityType) {
      case THUMBS_UP_DOC:
        this.activityData = activityEntity.getThumbsUpedDoc();
        break;
      case FOCUS_USER:
        this.activityData = activityEntity.getFocusedUser();
        break;
      case FOCUS_WIKI:
        this.activityData = activityEntity.getFocusedWiki();
        break;
      case CREATE_DOC:
        this.activityData = activityEntity.getCreatedDoc();
        break;
    }

    return this;
  }
}
