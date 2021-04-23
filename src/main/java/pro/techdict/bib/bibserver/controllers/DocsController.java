package pro.techdict.bib.bibserver.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.dtos.DocShowInListDto;
import pro.techdict.bib.bibserver.dtos.DocumentCommentDto;
import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.models.CommentModel;
import pro.techdict.bib.bibserver.models.DocumentMetaModel;
import pro.techdict.bib.bibserver.models.NewDocRequestModel;
import pro.techdict.bib.bibserver.services.DocumentService;
import pro.techdict.bib.bibserver.services.TencentCOSService;
import pro.techdict.bib.bibserver.utils.COSUploadResultWithKey;
import pro.techdict.bib.bibserver.utils.COSUtils;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/docs")
public class DocsController {

  private final TencentCOSService cosService;
  private final DocumentService documentService;

  public DocsController(
      TencentCOSService cosService,
      DocumentService documentService
  ) {
    this.cosService = cosService;
    this.documentService = documentService;
  }

  @GetMapping("/myList")
  public HttpResponse getMyDocumentList(
      @RequestParam Long userId,
      @RequestParam int pageNum
  ) {
    return HttpResponse.success(
        "获取我的文档列表成功！",
        documentService.getRecentRelativeDocumentList(userId, pageNum)
    );
  }

  @PostMapping("/")
  public HttpResponse newDoc(
      @RequestBody NewDocRequestModel requestBody
  ) {
    Long userId = requestBody.getUserId();
    Long wikiId = requestBody.getWikiId();
    try {
      DocumentViewData viewData = documentService.initializeNewDocument(userId, wikiId);
      return HttpResponse.success("新建文档成功！", viewData);
    } catch (Exception e) {
      return HttpResponse.fail("新建文档失败: " + e.getMessage());
    }
  }

  @GetMapping("/{docId}")
  public HttpResponse getDocumentViewData(
      @PathVariable Long docId,
      @RequestParam Long userId
  ) {
    try {
      return HttpResponse.success("获取文档信息成功！", documentService.getDocumentViewData(docId, userId));
    } catch (Exception e) {
      return HttpResponse.fail("获取文档信息失败: " + e.getMessage());
    }
  }

  @GetMapping("/thumbsUpedList")
  public HttpResponse getThumbsUpedDocs(
      @RequestParam Long userId,
      @RequestParam int pageNum
  ) {
    DocShowInListDto thumbsUpedDocumentList = documentService.getThumbsUpedDocumentList(userId, pageNum);
    return HttpResponse.success("获取点赞过的文档列表成功！", thumbsUpedDocumentList);
  }

  @PostMapping("/uploadImages")
  public HttpResponse uploadImages(
      @RequestParam("userId") String userId,
      @RequestParam("uploadImages") MultipartFile[] uploadFiles
  ) {
    List<COSUploadResultWithKey> uploadResults =
        cosService.uploadObjects(
            COSUtils.getPrefixWithUserId("bibweb/docs-images/", Long.parseLong(userId)),
            uploadFiles
        );
    Map<String, Object> data = new HashMap<>();
    data.put("uploadResults", uploadResults);
    return HttpResponse.success("上传图片成功！", data);
  }

  @PutMapping("/meta")
  public HttpResponse updateMeta(
      @RequestBody DocumentMetaModel meta
  ) {
    DocumentViewData newViewData = documentService.setDocumentMeta(meta);
    return HttpResponse.success("更新文档信息成功", newViewData);
  }

  @PutMapping("/thumbsUp")
  public HttpResponse thumbsUpDocument(
      @RequestBody Map<String, Long> requestBody
  ) {
    Long docId = requestBody.get("docId");
    Long userId = requestBody.get("userId");

    return documentService.thumbsUpDocument(userId, docId)
        ? HttpResponse.success()
        : HttpResponse.fail("点赞文档请求失败");
  }

  @PostMapping("/comment")
  public HttpResponse commentToDocument(
      @RequestBody CommentModel requestBody
  ) {
    DocumentCommentDto documentCommentDto = documentService.addCommentToDocument(requestBody);
    return documentCommentDto != null
        ? HttpResponse.success("评论文档成功！", documentCommentDto)
        : HttpResponse.fail("评论文档失败");
  }

}
