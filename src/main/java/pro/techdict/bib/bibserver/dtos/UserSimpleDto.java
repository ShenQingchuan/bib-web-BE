package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.entities.UserAccount;

@Data
public class UserSimpleDto {
  Long uid;
  String userName;
  UserDetailsSimpleDto userDetails;
  int followersCount;

  public UserSimpleDto fromEntity(UserAccount userEntity) {
    this.uid = userEntity.getId();
    this.userName = userEntity.getUserName();
    this.userDetails = new UserDetailsSimpleDto().fromEntity(userEntity.getUserDetails());
    this.followersCount = userEntity.getFollowers().size();
    return this;
  }
}
