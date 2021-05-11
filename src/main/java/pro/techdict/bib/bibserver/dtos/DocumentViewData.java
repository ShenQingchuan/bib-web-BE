package pro.techdict.bib.bibserver.dtos;

import lombok.Data;
import pro.techdict.bib.bibserver.entities.BaseEntity;
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
  List<Long> collaborators;
  List<DocumentCommentDto> comments;
  Boolean thumbsUped = false;
  WikiSimpleDto inWiki;
  Boolean publicSharing;

  public static DocumentViewData fromEntity(Document docEntity) {
    if (docEntity == null) return null;

    DocumentViewData viewData = new DocumentViewData();
    viewData.id = docEntity.getId();
    viewData.title = Objects.requireNonNullElse(docEntity.getTitle(), "");
    viewData.contentAbstract = Objects.requireNonNullElse(docEntity.getContentAbstract(), "");
    viewData.creator = UserSimpleDto.fromEntity(docEntity.getCreator());

    viewData.thumbUpUsers = Objects.requireNonNullElse(
        docEntity.getThumbUpUsers(),
        new ArrayList<UserAccount>()
    ).stream().map(UserSimpleDto::fromEntity).collect(Collectors.toList());

    viewData.collaborators = Objects.requireNonNullElse(
        docEntity.getCollaborators(),
        new ArrayList<UserAccount>()
    ).stream().map(BaseEntity::getId).collect(Collectors.toList());

    viewData.comments = Objects.requireNonNullElse(
        docEntity.getComments(),
        new ArrayList<DocumentComment>()
    ).stream().map(DocumentCommentDto::fromEntity).collect(Collectors.toList());

    viewData.inWiki = WikiSimpleDto.fromEntity(docEntity.getInWiki());
    viewData.publicSharing = docEntity.getPublicSharing();

    return viewData;
  }

  public DocumentViewData setIsThumbsUpedByUserId(Long userId) {
    this.setThumbsUped(this.thumbUpUsers.stream().anyMatch(user -> user.getUid().equals(userId)));
    return this;
  }

}
