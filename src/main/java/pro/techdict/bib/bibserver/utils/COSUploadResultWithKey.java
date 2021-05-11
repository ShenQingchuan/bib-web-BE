package pro.techdict.bib.bibserver.utils;

import com.qcloud.cos.model.PutObjectResult;
import lombok.Data;

@Data
public class COSUploadResultWithKey {
  String key;
  PutObjectResult putObjectResult;

  public COSUploadResultWithKey(String key, PutObjectResult putObjectResult) {
    this.key = key;
    this.putObjectResult = putObjectResult;
  }

  public String getFullURL() {
    return "https://techdict-1257165552.cos.ap-shanghai.myqcloud.com/" + this.key;
  }
}
