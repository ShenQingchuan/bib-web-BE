package pro.techdict.bib.bibserver.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;
import pro.techdict.bib.bibserver.beans.USER_ACTIVITY_TYPE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Table
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserActivity extends BaseEntity<Long> {

  @Column(nullable = false)
  USER_ACTIVITY_TYPE activityType;

  @ManyToOne
  UserAccount creator;

  @OneToOne
  Document thumbsUpedDoc;

  @OneToOne
  UserAccount focusedUser;

  @OneToOne
  Wiki focusedWiki;

  @OneToOne
  Document createdDoc;

}
