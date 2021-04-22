package pro.techdict.bib.bibserver.models;

import lombok.Data;

@Data
public class CommentModel {
  String content;
  Long docId;
  Long creatorId;
  Long replyToId;
}
