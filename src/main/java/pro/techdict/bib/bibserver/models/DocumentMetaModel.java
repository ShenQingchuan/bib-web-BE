package pro.techdict.bib.bibserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentMetaModel implements Serializable {
  Long docId;
  String title;
  String contentAbstract;
  Boolean publicSharing;
}
