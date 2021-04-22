package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.Organization;

@Data
@AllArgsConstructor
public class OrgSimpleDto {
  Long id;
  String name;
  String description;
  String avatarURL;
  Long creatorId;
  String creatorName;
  int memberCount;

  // ? 为什么不在这里传输成员列表
  // : 要在成员列表页面做分页查询

  public static OrgSimpleDto fromEntity(Organization orgEntity) {
    if (orgEntity == null) return null;

    return new OrgSimpleDto(
        orgEntity.getId(),
        orgEntity.getName(),
        orgEntity.getDescription(),
        orgEntity.getAvatarURL(),
        orgEntity.getCreator().getId(),
        orgEntity.getCreator().getUserName(),
        orgEntity.getMemberList().size()
    );
  }
}
