package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.dtos.DocumentViewData;
import pro.techdict.bib.bibserver.services.DocumentService;
import pro.techdict.bib.bibserver.services.TencentCOSService;
import pro.techdict.bib.bibserver.utils.COSUploadResultWithKey;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  @PostMapping("/uploadImages")
  public HttpResponse uploadImages(
      @RequestParam("userId") String userId,
      @RequestParam("uploadImages") MultipartFile[] uploadFiles
  ) {
    List<COSUploadResultWithKey> uploadResults =
        cosService.uploadObjects("bibweb/docs-images/", Long.parseLong(userId), uploadFiles);
    Map<String, Object> data = new HashMap<>();
    data.put("uploadResults", uploadResults);
    return HttpResponse.success("上传图片成功！", data);
  }

  @PostMapping("/new")
  public HttpResponse newDoc(
      @RequestBody Map<String, String> requestBody
  ) {
    Long userId = Long.parseLong(requestBody.get("userId"));
    DocumentViewData viewData = documentService.initializeNewDocument(userId);
    return HttpResponse.success("新建文档成功！", viewData);
  }

}
