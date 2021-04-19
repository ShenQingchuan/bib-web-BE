package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPES;
import pro.techdict.bib.bibserver.entities.UserActivity;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserActivityDto {
  Date createTime;
  USER_ACTIVITY_TYPES activityType;
  Object activityData;

  public static UserActivityDto fromEntity(UserActivity activityEntity) {
    USER_ACTIVITY_TYPES activityType = activityEntity.getActivityType();
    Object activityData = null;
    switch (activityType) {
      case THUMBS_UP_DOC:
        activityData = DocumentViewData.fromEntity(activityEntity.getThumbsUpedDoc());
        break;
      case FOCUS_USER:
        activityData = UserSimpleDto.fromEntity(activityEntity.getFocusedUser());
        break;
      case FOCUS_WIKI:
        activityData = WikiSimpleDto.fromEntity(activityEntity.getFocusedWiki());
        break;
      case CREATE_DOC:
        activityData = DocumentSimpleDto.fromEntity(activityEntity.getCreatedDoc());
        break;
      case CREATE_WIKI:
        activityData = WikiSimpleDto.fromEntity(activityEntity.getCreatedWiki());
        break;
    }

    return new UserActivityDto(
        activityEntity.getCreateTime(),
        activityType,
        activityData
    );
  }
}
