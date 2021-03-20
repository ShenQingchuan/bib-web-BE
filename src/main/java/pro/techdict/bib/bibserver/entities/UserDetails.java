package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonIgnore
  @OneToOne(mappedBy = "userDetails")
  private UserAccount userAccount;

  @JsonIgnore
  @UpdateTimestamp
  Date updateTime;
}
