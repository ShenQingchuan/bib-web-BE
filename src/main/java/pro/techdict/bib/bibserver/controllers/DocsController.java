package pro.techdict.bib.bibserver.controllers;

import com.qcloud.cos.model.PutObjectResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.services.TencentCOSService;
import pro.techdict.bib.bibserver.utils.COSUtils.ResultWithKey;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/docs")
public class DocsController {

  private final TencentCOSService cosService;

  public DocsController(TencentCOSService cosService) {
    this.cosService = cosService;
  }

  @PostMapping("/uploadImages")
  public HttpResponse multiImport(
      @RequestParam("userId") String userId,
      @RequestParam("uploadImages") MultipartFile[] uploadFiles
  ) {
    List<ResultWithKey> uploadResults =
        cosService.uploadObjects("bibweb/docs-images/", Long.parseLong(userId), uploadFiles);
    Map<String, Object> data = new HashMap<>();
    data.put("uploadResults", uploadResults);
    return HttpResponse.success("上传图片成功！", data);
  }
}
