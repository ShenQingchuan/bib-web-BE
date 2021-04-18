package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.entities.UserDetails;

@Data
public class UserDetailsSimpleDto {
  Long id;
  String avatarURL;
  String introduce;

  public UserDetailsSimpleDto fromEntity(UserDetails detailsEntity) {
    this.id = detailsEntity.getId();
    this.avatarURL = detailsEntity.getAvatarURL();
    this.introduce = detailsEntity.getIntroduce();
    return this;
  }
}
