package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentComment extends BaseEntity {

  String content; // 评论内容

  @ManyToOne
  @JsonIgnoreProperties({"comments"})
  Document target;

  @ManyToOne
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs"})
  UserAccount creator;

  @ManyToMany
  @JsonIgnoreProperties({"createdDocs", "collaborateDocs", "likedDocs"})
  List<UserAccount> thumbUpUsers;

}
