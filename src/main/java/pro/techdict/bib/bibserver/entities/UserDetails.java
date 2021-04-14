package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetails extends BaseEntity<Long> {

  String avatarURL;
  String introduce;
  String address;
  String profession;

  @JsonIgnore
  @OneToOne(mappedBy = "userDetails")
  private UserAccount userAccount;

}
