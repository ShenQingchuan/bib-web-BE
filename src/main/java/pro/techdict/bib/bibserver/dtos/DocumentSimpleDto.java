package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.Document;
import pro.techdict.bib.bibserver.entities.Wiki;

@Data
@AllArgsConstructor
public class DocumentSimpleDto {

  Long id;
  String title;
  String contentAbstract;
  UserSimpleDto creator;
  Boolean inWiki;
  String wikiName;
  Long updateTime;

  static DocumentSimpleDto fromEntity(Document docEntity) {
    Wiki inWiki = docEntity.getInWiki();
    return new DocumentSimpleDto(
        docEntity.getId(),
        docEntity.getTitle(),
        docEntity.getContentAbstract(),
        UserSimpleDto.fromEntity(docEntity.getCreator()),
        inWiki != null,
        inWiki != null ? docEntity.getInWiki().getName() : null,
        docEntity.getUpdateTime().getTime()
    );
  }

}
