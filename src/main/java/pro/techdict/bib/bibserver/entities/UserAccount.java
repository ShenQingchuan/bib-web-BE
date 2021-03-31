package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long uid;

  @Column(unique = true)
  String userName;

  @Column(unique = true)
  String phone;

  @Column(unique = true)
  String email;

  @JsonIgnore
  String password;

  String role;

  @JsonIgnore
  Date lastLoginTime;

  @CreationTimestamp
  @JsonIgnore
  Date registerDate;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JsonIgnoreProperties({"userAccount"})
  UserDetails userDetails; // 一用户对一详细信息

  @ManyToMany
  List<UserAccount> followers; // 多用户对多用户（关注者）

  // ———————— 用户 & 组织

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator", "memberList"})
  List<Organization> createdOrgs; // 一用户对多组织（创建的）

  @ManyToMany(mappedBy = "memberList")
  @JsonIgnoreProperties({"creator", "memberList"})
  List<Organization> joinedOrgs; // 多用户对多组织（加入的）

  // ———————— 用户 & 文档

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers"})
  List<Document> createdDocs; // 一用户对多文档（创建的）

  @ManyToMany(mappedBy = "collaborators")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers"})
  List<Document> collaborateDocs; // 多协作者对多文档

  @ManyToMany(mappedBy = "thumbUpUsers")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers"})
  List<Document> likedDocs; // 多用户对多文档（点赞的）

  @ManyToMany(mappedBy = "starUsers")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers"})
  List<Document> staredDocs; // 多用户对多文档（收藏的）

  // ———————— 用户 & 知识库

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator", "followers"})
  List<Wiki> createdWikis; // 一用户对多知识库（创建的）

  @ManyToMany(mappedBy = "followers")
  @JsonIgnoreProperties({"creator", "followers"})
  List<Wiki> focusingWikis;

  // ———————— 用户 & 评论

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator"})
  List<DocumentComment> createdComments; // 一用户对多评论

  @ManyToMany(mappedBy = "thumbUpUsers")
  @JsonIgnoreProperties({"creator"})
  List<DocumentComment> likedComments; // 多用户对多点赞评论

}
