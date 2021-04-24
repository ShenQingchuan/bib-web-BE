package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.DocShowInWikiOnePageDto;
import pro.techdict.bib.bibserver.dtos.WikiListOnePageData;
import pro.techdict.bib.bibserver.dtos.WikiSimpleDto;
import pro.techdict.bib.bibserver.dtos.WikiViewDataDto;
import pro.techdict.bib.bibserver.models.CreateWikiFormModel;

public interface WikiService {

  WikiSimpleDto createNewWiki(CreateWikiFormModel formModel);

  WikiViewDataDto getWikiViewData(Long wikiId, Long userId);

  WikiListOnePageData getWikiShowList(Long userId, int pageNum);

  DocShowInWikiOnePageDto getWikiAllDocsByPage(Long wikiId, int pageNum);

}
