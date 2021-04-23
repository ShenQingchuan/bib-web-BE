package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.*;
import pro.techdict.bib.bibserver.models.CreateWikiFormModel;
import pro.techdict.bib.bibserver.services.WikiService;
import pro.techdict.bib.bibserver.utils.HttpResponse;

@RestController
@RequestMapping("/wiki")
public class WikiController {

  final WikiService wikiService;

  public WikiController(WikiService wikiService) {
    this.wikiService = wikiService;
  }

  @PostMapping("/")
  public HttpResponse createNewWiki(
      @RequestBody CreateWikiFormModel formModel
  ) {
    return HttpResponse.success(
        "新建知识库成功！",
        wikiService.createNewWiki(formModel)
    );
  }

  @GetMapping("/myAll")
  public HttpResponse getAllMyRelativeWikis(
    @RequestParam Long userId,
    @RequestParam int pageNum
  ) {
    return HttpResponse.success(
        "获取所有相关知识库成功！",
        wikiService.getWikiShowList(userId, pageNum)
    );
  }

  @GetMapping("/allDocs")
  public HttpResponse getWikiAllDocuments(
      @RequestParam Long wikiId,
      @RequestParam int pageNum
  ) {
    return HttpResponse.success("获取知识库文档列表成功！",
      wikiService.getWikiAllDocsByPage(wikiId, pageNum)
    );
  }

}
