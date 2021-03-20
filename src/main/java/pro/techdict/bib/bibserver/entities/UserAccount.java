package pro.techdict.bib.bibserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long uid;

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

    @JsonIgnore
    @CreationTimestamp
    Date registerDate;

    @JsonIgnoreProperties({ "userAccount" })
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name="detailsId", referencedColumnName = "detailsId")
    UserDetails userDetails;

    @JsonIgnoreProperties({ "memberList", "creator" })
    @OneToMany(mappedBy = "creator")
    List<Organization> createdOrgs;

    @JsonIgnoreProperties({ "memberList", "creator" })
    @ManyToMany(mappedBy = "memberList")
    List<Organization> joinedOrgs;
}
