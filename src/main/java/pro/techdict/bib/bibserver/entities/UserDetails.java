package pro.techdict.bib.bibserver.entities;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long detailsId;

  String avatarURL;
  String introduce;
  String address;
  String profession;

  @Transient
  @OneToOne(mappedBy = "userDetails")
  private UserAccount userAccount;

  @UpdateTimestamp
  Date updateTime;
}
