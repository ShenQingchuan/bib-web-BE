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
  DocumentCommentDto replyTo;
  int thumbsUpCount;
  Long createTime;

  public static DocumentCommentDto fromEntity(DocumentComment commentEntity) {
    if (commentEntity == null) return null;

    int thumbsUpCount = 0;
    if (commentEntity.getThumbUpUsers() != null) {
      thumbsUpCount = commentEntity.getThumbUpUsers().size();
    }
    return new DocumentCommentDto(
        commentEntity.getId(),
        commentEntity.getContent(),
        UserSimpleDto.fromEntity(commentEntity.getCreator()),
        DocumentCommentDto.fromEntity(commentEntity.getReplyTo()),
        thumbsUpCount,
        commentEntity.getCreateTime().getTime()
    );
  }
}
