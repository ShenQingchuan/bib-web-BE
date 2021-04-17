package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.entities.DocumentComment;

@Data
public class DocumentCommentDto {
  Long id;
  String content;
  UserSimpleDto creator;

  public DocumentCommentDto fromEntity(DocumentComment commentEntity) {
    this.id = commentEntity.getId();
    this.content = commentEntity.getContent();
    this.creator = new UserSimpleDto().fromEntity(commentEntity.getCreator());
    return this;
  }
}
