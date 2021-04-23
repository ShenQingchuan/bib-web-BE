package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocShowInWikiOnePageDto {
  List<DocShowInWikiListDto> items;
  int pageTotal;
}
