package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPES;

import javax.persistence.*;

@Table
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserActivity extends BaseEntity<Long> {

  @Column(nullable = false)
  USER_ACTIVITY_TYPES activityType;

  @ManyToOne
  UserAccount creator;

  @OneToOne
  @JsonIgnoreProperties({"creator"})
  Document thumbsUpedDoc;

  @OneToOne
  @JsonIgnoreProperties({"activities"})
  UserAccount focusedUser;

  @OneToOne
  @JsonIgnoreProperties({"creator"})
  Wiki focusedWiki;

  @OneToOne
  @JsonIgnoreProperties({"creator"})
  Document createdDoc;

}
