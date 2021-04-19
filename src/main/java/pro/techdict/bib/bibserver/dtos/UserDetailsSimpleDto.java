package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.UserDetails;

@Data
@AllArgsConstructor
public class UserDetailsSimpleDto {
  Long id;
  String avatarURL;
  String introduce;

  public static UserDetailsSimpleDto fromEntity(UserDetails detailsEntity) {
    return new UserDetailsSimpleDto(
        detailsEntity.getId(),
        detailsEntity.getAvatarURL(),
        detailsEntity.getIntroduce()
    );
  }
}
