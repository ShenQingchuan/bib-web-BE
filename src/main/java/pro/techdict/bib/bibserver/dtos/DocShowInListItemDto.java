package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pro.techdict.bib.bibserver.beans.ARCHIVE_TYPES;
import pro.techdict.bib.bibserver.entities.Document;
import pro.techdict.bib.bibserver.entities.UserAccount;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class DocShowInListItemDto {

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  static class ListItem {
    Long id;
    String title;
    Long creatorId;
    String creatorName;
    Long createTime;
    Long updateTime;
    ARCHIVE_TYPES archiveType;
    Boolean editable = false;
    Long wikiId;
    String wikiName;
    Long orgId;
    String orgName;

    static ListItem fromEntity(Document documentEntity, Long userId) {
      ListItem item = new ListItem();
      item.setId(documentEntity.getId());
      item.setTitle(documentEntity.getTitle());
      item.setCreatorId(documentEntity.getCreator().getId());
      item.setCreatorName(documentEntity.getCreator().getUserName());
      item.setCreateTime(documentEntity.getCreateTime().getTime());
      item.setUpdateTime(documentEntity.getUpdateTime().getTime());

      List<UserAccount> collaborators = documentEntity.getCollaborators();
      collaborators.add(documentEntity.getCreator());
      item.setEditable(collaborators.stream().anyMatch(c -> c.getId().equals(userId)));

      if (documentEntity.getInWiki() != null) {
        item.setWikiId(documentEntity.getInWiki().getId());
        item.setWikiName(documentEntity.getInWiki().getName());
        if (documentEntity.getInWiki().getOrganization() != null) {
          item.setArchiveType(ARCHIVE_TYPES.ORG_WIKI);
          item.setOrgId(documentEntity.getInWiki().getOrganization().getId());
          item.setOrgName(documentEntity.getInWiki().getOrganization().getName());
        } else {
          item.setArchiveType(ARCHIVE_TYPES.USER_WIKI);
        }
      } else {
        item.setArchiveType(ARCHIVE_TYPES.USER_ONLY);
      }

      return item;
    }
  }

  List<ListItem> items;
  int pageTotal;

  public static DocShowInListItemDto fromEntities(Long userId, List<Document> docEntities, int pageTotal) {
    List<ListItem> listItems = docEntities.stream().map(
        doc -> ListItem.fromEntity(doc, userId)
    ).collect(Collectors.toList());
    return new DocShowInListItemDto(listItems, pageTotal);
  }

}
