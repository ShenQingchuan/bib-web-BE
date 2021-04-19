package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.WikiSimpleDto;
import pro.techdict.bib.bibserver.models.CreateWikiFormModel;

public interface WikiService {

  WikiSimpleDto createNewWiki(CreateWikiFormModel formModel);

}
