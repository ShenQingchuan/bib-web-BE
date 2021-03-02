package pro.techdict.bib.bibserver.services;

import org.springframework.web.multipart.MultipartFile;
import pro.techdict.bib.bibserver.utils.COSUtils.ResultWithKey;

import java.util.List;

public interface TencentCOSService {

  List<ResultWithKey> uploadObjects(String directory, long userId, MultipartFile[] files);

}
