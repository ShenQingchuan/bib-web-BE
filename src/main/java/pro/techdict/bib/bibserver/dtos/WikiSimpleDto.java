package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.Wiki;

@Data
@AllArgsConstructor
public class WikiSimpleDto {
  Long id;
  String name;
  Boolean isPrivate;

  public static WikiSimpleDto fromEntity(Wiki wikiEntity) {
    if (wikiEntity == null) return null;

    return new WikiSimpleDto(
        wikiEntity.getId(),
        wikiEntity.getName(),
        wikiEntity.getIsPrivate()
    );
  }
}
