package pro.techdict.bib.bibserver.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.models.DocumentMetaModel;
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
        documentService.getDocumentList(userId, pageNum)
    );
  }

  @GetMapping("/{docId}")
  public HttpResponse getDocumentViewData(
      @PathVariable Long docId
  ) {
    try {
      return HttpResponse.success("获取文档信息成功！", documentService.getDocumentViewData(docId));
    } catch (Exception e) {
      return HttpResponse.fail("获取文档信息失败: " + e.getMessage());
    }
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

  @PostMapping("/")
  public HttpResponse newDoc(
      @RequestBody Map<String, String> requestBody
  ) {
    Long userId = Long.parseLong(requestBody.get("userId"));
    try {
      DocumentViewData viewData = documentService.initializeNewDocument(userId);
      return HttpResponse.success("新建文档成功！", viewData);
    } catch (Exception e) {
      return HttpResponse.fail("新建文档失败: " + e.getMessage());
    }
  }

  @PutMapping("/meta")
  public HttpResponse updateMeta(
      @RequestBody DocumentMetaModel meta
  ) {
    DocumentViewData newViewData = documentService.setDocumentMeta(meta);
    return HttpResponse.success("更新文档信息成功", newViewData);
  }

}
