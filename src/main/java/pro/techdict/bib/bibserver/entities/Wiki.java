package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Wiki extends BaseEntity<Long> {

  String name; // 知识库名
  String description; // 知识库介绍
  Boolean isPrivate; // 是否私有库

  @ManyToOne
  @JsonIgnoreProperties({"createdWikis", "focusingWikis"})
  UserAccount creator; // 多知识库对一用户（作者）

  @OneToMany(mappedBy = "inWiki")
  List<Document> documents; // 一知识库对多文档

  @ManyToMany
  @JsonIgnoreProperties({"createdWikis", "focusingWikis"})
  List<UserAccount> followers; // 多知识库对多用户（关注者）

}
