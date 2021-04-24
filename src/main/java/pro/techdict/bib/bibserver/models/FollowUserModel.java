package pro.techdict.bib.bibserver.models;

import lombok.Data;

@Data
public class FollowUserModel {
  Long srcUid;
  String targetUserName;
}
