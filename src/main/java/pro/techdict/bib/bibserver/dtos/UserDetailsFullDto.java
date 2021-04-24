package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserDetails;

@Data
@AllArgsConstructor
public class UserDetailsFullDto {
  Long id;
  String avatarURL;
  String introduce;
  String address;
  String profession;
  Boolean isFollowing;

  int followersCount;
  int followingsCount;

  public static UserDetailsFullDto fromEntity(UserDetails detailsEntity) {
    if (detailsEntity == null) return null;

    return new UserDetailsFullDto(
        detailsEntity.getId(),
        detailsEntity.getAvatarURL(),
        detailsEntity.getIntroduce(),
        detailsEntity.getAddress(),
        detailsEntity.getProfession(),
        false,
        0,0
    );
  }

  public UserDetailsFullDto setFAndSCount(@NotNull UserAccount userEntity) {
    this.followersCount = userEntity.getFollowers().size();
    this.followingsCount = userEntity.getFollowings().size();
    return this;
  }
}
