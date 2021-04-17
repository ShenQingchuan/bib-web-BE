package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.entities.Document;
import pro.techdict.bib.bibserver.entities.DocumentComment;
import pro.techdict.bib.bibserver.entities.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class DocumentViewData {
  Long id;
  String title;
  String contentAbstract;
  UserSimpleDto creator;
  List<UserSimpleDto> thumbUpUsers;
  List<UserSimpleDto> collaborators;
  List<DocumentCommentDto> comments;
  Boolean thumbsUped = false;
  WikiSimpleDto inWiki;
  Boolean publicSharing;

  public DocumentViewData fromEntity(Document docEntity) {
    if (docEntity == null) return null;

    this.id = docEntity.getId();
    this.title = Objects.requireNonNullElse(docEntity.getTitle(), "");
    this.contentAbstract = Objects.requireNonNullElse(docEntity.getContentAbstract(), "");
    this.creator = new UserSimpleDto().fromEntity(docEntity.getCreator());

    this.thumbUpUsers = Objects.requireNonNullElse(
        docEntity.getThumbUpUsers(),
        new ArrayList<UserAccount>()
    ).stream().map(
        thumbsUpUser -> new UserSimpleDto().fromEntity(thumbsUpUser)
    ).collect(Collectors.toList());

    this.collaborators = Objects.requireNonNullElse(
        docEntity.getCollaborators(),
        new ArrayList<UserAccount>()
    ).stream().map(
        collaborator -> new UserSimpleDto().fromEntity(collaborator)
    ).collect(Collectors.toList());

    this.comments = Objects.requireNonNullElse(
        docEntity.getComments(),
        new ArrayList<DocumentComment>()
    ).stream().map(
        comment -> new DocumentCommentDto().fromEntity(comment)
    ).collect(Collectors.toList());

    this.inWiki = new WikiSimpleDto().fromEntity(docEntity.getInWiki());
    this.publicSharing = docEntity.getPublicSharing();

    return this;
  }
}
