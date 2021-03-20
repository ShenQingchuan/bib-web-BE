package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pro.techdict.bib.bibserver.models.OrganizationScope;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
public class Organization {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @Column(unique = true)
  String name;

  String description;
  OrganizationScope scope;
  String avatarURL;

  @JsonIgnore
  @CreationTimestamp
  Date createTime;

  @JsonIgnore
  @UpdateTimestamp
  Date lastUpdateTime;

  @ManyToOne
  @JsonIgnoreProperties({ "createdOrgs", "joinedOrgs" })
  UserAccount creator;

  @ManyToMany
  @JsonIgnoreProperties({ "createdOrgs", "joinedOrgs" })
  List<UserAccount> memberList;
}
