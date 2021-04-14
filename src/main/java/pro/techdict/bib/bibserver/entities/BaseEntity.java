package pro.techdict.bib.bibserver.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity<IDT extends Serializable> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  IDT id;

  @CreationTimestamp
  Date createTime;

  @UpdateTimestamp
  Date updateTime;

  int entityStatus = 0;

}
