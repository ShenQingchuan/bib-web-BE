package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.beans.WIKI_TYPE;
import pro.techdict.bib.bibserver.entities.Wiki;

@Data
@AllArgsConstructor
public class WikiListItemDataDto {
  Long id;
  String name;
  String description;
  Boolean isPrivate;
  WIKI_TYPE wikiType;
  Long updateTime;
  OrgSimpleDto organization;

  public static WikiListItemDataDto fromEntity(Wiki wikiEntity) {
    WIKI_TYPE wikiType = WIKI_TYPE.USER_WIKI;
    if (wikiEntity.getOrganization() != null) {
      wikiType = WIKI_TYPE.ORG_WIKI;
    }

    return new WikiListItemDataDto(
        wikiEntity.getId(),
        wikiEntity.getName(),
        wikiEntity.getDescription(),
        wikiEntity.getIsPrivate(),
        wikiType,
        wikiEntity.getUpdateTime().getTime(),
        OrgSimpleDto.fromEntity(wikiEntity.getOrganization())
    );
  }

}
