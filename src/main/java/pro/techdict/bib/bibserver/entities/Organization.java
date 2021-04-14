package pro.techdict.bib.bibserver.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pro.techdict.bib.bibserver.models.OrganizationScope;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class Organization extends BaseEntity<Long> {

  @Column(unique = true)
  String name;

  String description;
  OrganizationScope scope;
  String avatarURL;

  int entityStatus = 0;

  @ManyToOne
  UserAccount creator;

  @ManyToMany
  List<UserAccount> memberList;

}
