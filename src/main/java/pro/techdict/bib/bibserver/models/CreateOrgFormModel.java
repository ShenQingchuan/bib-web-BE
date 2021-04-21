package pro.techdict.bib.bibserver.models;

import lombok.Data;

@Data
public class CreateOrgFormModel {
  String name;
  String desc;
  String avatarURL;
  Long creatorUid;
}
