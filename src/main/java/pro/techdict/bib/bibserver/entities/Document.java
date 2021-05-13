package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Document extends BaseEntity<Long> {

  String title;
  String contentAbstract;

  @Column(nullable = false)
  Boolean publicSharing = false; // 是否能分享为公开阅读

  @ManyToOne
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs", "joinRequests"})
  UserAccount creator; // 多文档对一用户（作者）

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs", "joinRequests"})
  List<UserAccount> thumbUpUsers; // 多文档对多用户（点赞）

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs", "joinRequests"})
  List<UserAccount> collaborators; // 多文档对多用户（协作）

  @OneToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs", "joinRequests"})
  Set<UserAccount> pendingRequests;

  @ManyToOne
  @JsonIgnoreProperties({"documents"})
  Wiki inWiki; // 多文档对一知识库

  @OneToMany(mappedBy = "target")
  @JsonIgnoreProperties({ "target" })
  List<DocumentComment> comments; // 一文档对多评论

}
