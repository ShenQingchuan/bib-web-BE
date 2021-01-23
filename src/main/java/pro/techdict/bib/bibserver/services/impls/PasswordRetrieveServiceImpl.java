package pro.techdict.bib.bibserver.services.impls;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ses.v20201002.SesClient;
import com.tencentcloudapi.ses.v20201002.models.SendEmailRequest;
import com.tencentcloudapi.ses.v20201002.models.SendEmailResponse;
import com.tencentcloudapi.ses.v20201002.models.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.techdict.bib.bibserver.configs.TencentCloudSecretProperties;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.services.PasswordRetrieveService;
import pro.techdict.bib.bibserver.services.RedisService;

import java.util.Random;

@Slf4j
@Service
public class PasswordRetrieveServiceImpl implements PasswordRetrieveService {
  private final RedisService redisService;
  private final TencentCloudSecretProperties tencentCloudSecretProperties;

  private final String passwordRetrievePrefix = "PRVCode:";

  public PasswordRetrieveServiceImpl(RedisService redisService, TencentCloudSecretProperties tencentCloudSecretProperties) {
    this.redisService = redisService;
    this.tencentCloudSecretProperties = tencentCloudSecretProperties;
  }

  /**
   * 为用户的找回密码请求生成一个验证码存入 Redis 并发送到用户邮箱地址
   *
   * @param email 用户邮箱地址
   */
  @Override
  public void generateVerifyCodeByEmail(String email) {
    StringBuilder stringBuilder = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 6; i++) {
      stringBuilder.append(random.nextInt(10));
    }

    long passwordRetrieveExpires = 7200; // 验证码 2 小时过期
    // 发送邮件
    sendVerifyCodeEmailByTencentCloud(stringBuilder.toString(), email);
    // 验证码并存储到 redis 中
    redisService.set(passwordRetrievePrefix + email, stringBuilder.toString());
    redisService.expire(passwordRetrievePrefix + email, passwordRetrieveExpires);
  }

  /**
   * @param email       作为 Redis 查询的 key 的邮箱地址
   * @param requestCode 请求中用户输入的验证码
   * @return 返回是否验证正确
   */
  @Override
  public boolean verifyAuthCodeForEmail(String email, String requestCode) {
    if (!StringUtils.hasLength(email)) return false;
    String savedCode = redisService.get(passwordRetrievePrefix + email);
    if (savedCode != null && savedCode.equals(requestCode)) {
      redisService.remove(passwordRetrievePrefix + email);
      return true;
    }
    return false;
  }

  public void sendVerifyCodeEmailByTencentCloud(String sendCode, String destination) {
    try {
      Credential cred = new Credential(
          tencentCloudSecretProperties.getSecretId(),
          tencentCloudSecretProperties.getSecretKey()
      );

      HttpProfile httpProfile = new HttpProfile();
      httpProfile.setEndpoint("ses.tencentcloudapi.com");

      ClientProfile clientProfile = new ClientProfile();
      clientProfile.setHttpProfile(httpProfile);

      SesClient client = new SesClient(cred, "ap-hongkong", clientProfile);
      SendEmailRequest req = new SendEmailRequest();
      String[] destination1 = {destination};
      req.setDestination(destination1);

      Template template1 = new Template();
      template1.setTemplateID(13042L);
      template1.setTemplateData("{\"vcode\":\"" + sendCode + "\"}");
      req.setTemplate(template1);

      req.setFromEmailAddress("bib@mailsender.techdict.pro");
      req.setSubject("「 验证码 」· 找回密码 - Bib 云知识库");

      SendEmailResponse resp = client.SendEmail(req);
      log.info("Send Email Response: " + SendEmailResponse.toJsonString(resp));
    } catch (TencentCloudSDKException e) {
      throw new CustomException(CustomExceptionType.TENCENT_CLOUD_SDK_ERROR);
    }
  }
}
