package pro.techdict.bib.bibserver.services;

import pro.techdict.bib.bibserver.dtos.*;
import pro.techdict.bib.bibserver.models.CreateWikiFormModel;

public interface WikiService {

  WikiSimpleDto createNewWiki(CreateWikiFormModel formModel);

  WikiViewDataDto getWikiViewData(Long wikiId, Long readerId);

  PageDto<WikiListItemDataDto> getWikiShowList(Long userId, int pageNum);

  PageDto<DocShowInWikiListDto> getWikiAllDocsByPage(Long wikiId, int pageNum);

}
