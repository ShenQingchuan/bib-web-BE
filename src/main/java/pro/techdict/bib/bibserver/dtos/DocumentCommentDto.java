package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.DocumentComment;

@Data
@AllArgsConstructor
public class DocumentCommentDto {
  Long id;
  String content;
  UserSimpleDto creator;

  public static DocumentCommentDto fromEntity(DocumentComment commentEntity) {
    return new DocumentCommentDto(
        commentEntity.getId(),
        commentEntity.getContent(),
        UserSimpleDto.fromEntity(commentEntity.getCreator())
    );
  }
}
