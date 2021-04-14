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
public class Document extends BaseEntity<Long> {

  String title;
  String contentAbstract;

  @Column(columnDefinition = "MEDIUMTEXT")
  String contentJSON; // ProseMirror 导出 doc.toJSON()

  Boolean publicSharing; // 是否能分享为公开阅读

  @ManyToOne
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs"})
  UserAccount creator; // 多文档对一用户（作者）

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs"})
  List<UserAccount> thumbUpUsers; // 多文档对多用户（点赞）

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs"})
  List<UserAccount> collaborators; // 多文档对多用户（协作）

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs"})
  List<UserAccount> starUsers; // 多文档对多用户（收藏）

  @ManyToOne
  @JsonIgnoreProperties({"documents"})
  Wiki inWiki; // 多文档对一知识库

  @OneToMany(mappedBy = "target")
  @JsonIgnoreProperties({ "target" })
  List<DocumentComment> comments; // 一文档对多评论

}
