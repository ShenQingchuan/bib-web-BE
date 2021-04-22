package pro.techdict.bib.bibserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WikiListOnePageData {
  List<WikiListItemDataDto> items;
  int pageTotal;
}
