package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentComment extends BaseEntity<Long> {

  String content; // 评论内容

  @ManyToOne
  @JsonIgnore
  Document target;

  @ManyToOne
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs"})
  UserAccount creator;

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs", "staredDocs"})
  List<UserAccount> thumbUpUsers;

  @OneToOne
  @JsonIgnoreProperties({"replyTo"})
  DocumentComment replyTo;

}
