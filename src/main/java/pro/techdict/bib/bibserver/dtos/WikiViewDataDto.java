package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import pro.techdict.bib.bibserver.entities.Wiki;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class WikiViewDataDto {
  Long id;
  String name;
  String description;
  Boolean isPrivate;
  Boolean joined;
  Boolean focused;
  int focusCount;
  UserSimpleDto creator;
  List<DocumentSimpleDto> docs;

  public static WikiViewDataDto fromEntityAndUserId(Wiki wikiEntity, Long userId) {
    if (wikiEntity == null) return null;

    boolean focused = wikiEntity.getFollowers().stream()
        .anyMatch(follower -> follower.getId().equals(userId));
    boolean joined = wikiEntity.getCreator().getId().equals(userId);
    if (wikiEntity.getOrganization() != null) {
      joined = wikiEntity.getOrganization().getMemberList()
          .stream().anyMatch(user -> user.getId().equals(userId));
    }

    return new WikiViewDataDto(
        wikiEntity.getId(),
        wikiEntity.getName(),
        wikiEntity.getDescription(),
        wikiEntity.getIsPrivate(),
        joined,
        focused,
        wikiEntity.getFollowers().size(),
        UserSimpleDto.fromEntity(wikiEntity.getCreator()),
        wikiEntity.getDocuments().stream()
            .map(DocumentSimpleDto::fromEntity).collect(Collectors.toList())
    );
  }

}
