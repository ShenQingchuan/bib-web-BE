package pro.techdict.bib.bibserver.services;

import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.utils.COSUploadResultWithKey;

import java.util.List;

public interface TencentCOSService {

  List<COSUploadResultWithKey> uploadObjects(String directory, long userId, MultipartFile[] files);

}
