package pro.techdict.bib.bibserver.services.impls;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.configs.TencentCloudProperties;
import pro.techdict.bib.bibserver.services.TencentCOSService;
import pro.techdict.bib.bibserver.utils.COSUploadResultWithKey;
import pro.techdict.bib.bibserver.utils.COSUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class  TencentCOSServiceImpl implements TencentCOSService {

  private final TencentCloudProperties tencentCloudProperties;

  public TencentCOSServiceImpl(TencentCloudProperties tencentCloudProperties) {
    this.tencentCloudProperties = tencentCloudProperties;
  }

  /**
   * @param directory 上传目录，必须以 "/" 结尾
   * @param userId    上传用户唯一标识
   * @param files     文件集合
   */
  @Override
  public List<COSUploadResultWithKey> uploadObjects(String keyPrefix, MultipartFile[] files) {
    COSCredentials credentials = new BasicCOSCredentials(
        tencentCloudProperties.getSecretId(),
        tencentCloudProperties.getSecretKey()
    );
    ClientConfig clientConfig = new ClientConfig(new Region(tencentCloudProperties.getCosRegion()));
    COSClient cosClient = new COSClient(credentials, clientConfig);

    List<COSUploadResultWithKey> results = new ArrayList<>();
    for (MultipartFile file : files) {
      try {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        String key = COSUtils.getKeyWithPrefix(keyPrefix, file);

        PutObjectRequest putObjectRequest = new PutObjectRequest(
            tencentCloudProperties.getCosBucket(),
            key,
            file.getInputStream(), metadata
        );
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        COSUploadResultWithKey resultWithKey = new COSUploadResultWithKey(
            key,
            putObjectResult
        );
        results.add(resultWithKey);
      } catch (IOException e) {
        return null;
      }
    }
    return results;
  }
}
