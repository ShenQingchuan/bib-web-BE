package pro.techdict.bib.bibserver.models;

import lombok.Data;

@Data
public class CreateWikiFormModel {
  Long userId;
  String name;
  String desc;
  int scope; // 0 私有，1 公开
}
