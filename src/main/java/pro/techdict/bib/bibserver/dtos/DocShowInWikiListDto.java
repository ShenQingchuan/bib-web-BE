package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.Document;

@Data
@AllArgsConstructor
public class DocShowInWikiListDto {
  Long id;
  String title;
  UserSimpleDto creator;
  Long updateTime;
  Boolean publicSharing;

  public static DocShowInWikiListDto fromEntity(Document docEntity) {
    if (docEntity == null) return null;

    return new DocShowInWikiListDto(
        docEntity.getId(),
        docEntity.getTitle(),
        UserSimpleDto.fromEntity(docEntity.getCreator()),
        docEntity.getUpdateTime().getTime(),
        docEntity.getPublicSharing()
    );
  }
}
