package pro.techdict.bib.bibserver.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class COSUtils {

  /**
   * @param directory 上传目录，必须以 "/" 结尾
   * @param userId    上传用户唯一标识
   * @param file      文件
   */
  public static String getPrefixWithUserId(String directory, long userId) {
    return directory + "uid-" + userId + "/" + new Date().getTime();
  }

  public static String getKeyWithPrefix(String keyPrefix, MultipartFile file) {
    return keyPrefix + "__" + file.getOriginalFilename();
  }
}
