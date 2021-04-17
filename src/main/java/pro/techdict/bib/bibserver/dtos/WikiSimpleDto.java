package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.entities.Wiki;

@Data
public class WikiSimpleDto {
  Long id;
  String name;
  Boolean isPrivate;

  public WikiSimpleDto fromEntity(Wiki wikiEntity) {
    if (wikiEntity == null) return null;

    this.id = wikiEntity.getId();
    this.name = wikiEntity.getName();
    this.isPrivate = wikiEntity.getIsPrivate();

    return this;
  }
}
