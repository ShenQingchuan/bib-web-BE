package pro.techdict.bib.bibserver.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Transient;
import javax.persistence.*;
import java.util.Date;

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

    @Transient
    String password;
    String role;

    Date lastLoginTime;

    @CreationTimestamp
    Date registerDate;

}
