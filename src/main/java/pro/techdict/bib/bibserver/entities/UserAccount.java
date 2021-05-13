package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"userDetails"})
public class UserAccount extends BaseEntity<Long> {

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

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  @JsonIgnoreProperties({"userAccount"})
  UserDetails userDetails; // 一用户对一详细信息

  @ManyToMany
  @JoinTable(name="user_following_relations",
      joinColumns={@JoinColumn(name="follower_id")},
      inverseJoinColumns={@JoinColumn(name="following_id")})
  List<UserAccount> followers; // 多用户对多用户（关注者）

  @ManyToMany(mappedBy = "followers")
  List<UserAccount> followings;

  // ———————— 用户 & 组织

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator", "memberList"})
  List<Organization> createdOrgs; // 一用户对多组织（创建的）

  @ManyToMany(mappedBy = "memberList")
  @JsonIgnoreProperties({"creator", "memberList"})
  List<Organization> joinedOrgs; // 多用户对多组织（加入的）

  // ———————— 用户 & 文档

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers", "pendingRequests"})
  List<Document> createdDocs; // 一用户对多文档（创建的）

  @ManyToMany(mappedBy = "collaborators")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers", "pendingRequests"})
  List<Document> collaborateDocs; // 多协作者对多文档

  @ManyToMany(mappedBy = "thumbUpUsers")
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers", "pendingRequests"})
  List<Document> likedDocs; // 多用户对多文档（点赞的）

  @OneToMany
  @JsonIgnoreProperties({"creator", "collaborators", "thumbUpUsers", "starUsers", "pendingRequests"})
  List<Document> joinRequests;

  // ———————— 用户 & 知识库

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator", "followers"})
  List<Wiki> createdWikis; // 一用户对多知识库（创建的）

  @ManyToMany(mappedBy = "followers")
  @JsonIgnoreProperties({"creator", "followers"})
  List<Wiki> focusingWikis;  // 多用户对多知识库（关注的）

  // ———————— 用户 & 评论

  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator"})
  List<DocumentComment> createdComments; // 一用户对多评论

  @ManyToMany(mappedBy = "thumbUpUsers")
  @JsonIgnoreProperties({"creator"})
  List<DocumentComment> likedComments; // 多用户对多点赞评论

  // ———————— 用户 & 评论
  @OneToMany(mappedBy = "creator")
  @JsonIgnoreProperties({"creator"})
  List<UserActivity> activities; // 一用户对多动态

}
