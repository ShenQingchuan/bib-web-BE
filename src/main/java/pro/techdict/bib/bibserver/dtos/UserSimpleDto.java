package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.UserAccount;

@Data
@AllArgsConstructor
public class UserSimpleDto {
  Long uid;
  String userName;
  UserDetailsSimpleDto userDetails;
  int followersCount;

  public static UserSimpleDto fromEntity(UserAccount userEntity) {
    return new UserSimpleDto(
        userEntity.getId(),
        userEntity.getUserName(),
        UserDetailsSimpleDto.fromEntity(userEntity.getUserDetails()),
        userEntity.getFollowers().size()
    );
  }
}
