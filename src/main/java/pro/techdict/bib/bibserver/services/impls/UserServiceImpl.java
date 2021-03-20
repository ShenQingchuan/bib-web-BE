package pro.techdict.bib.bibserver.services.impls;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.configs.TencentCloudProperties;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.entities.Organization;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserDetails;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.models.DUPLICATE_TYPES;
import pro.techdict.bib.bibserver.services.RedisService;
import pro.techdict.bib.bibserver.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
  private final RedisService redisService;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final TencentCloudProperties tencentCloudProperties;

  public final static String smsCodePrefix = "SMSCode:+86";

  public UserServiceImpl(
      RedisService redisService, BCryptPasswordEncoder bCryptPasswordEncoder,
      UserRepository userRepository,
      TencentCloudProperties tencentCloudProperties
  ) {
    this.redisService = redisService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userRepository = userRepository;
    this.tencentCloudProperties = tencentCloudProperties;
  }

  @Override
  public UserAccount registerUser(String userName, String password, String phone, String email) {
    UserAccount user = new UserAccount();
    user.setUserName(userName);
    user.setPassword(bCryptPasswordEncoder.encode(password));
    user.setPhone(phone);
    user.setEmail(email);
    user.setRole("ROLE_USER");
    user.setJoinedOrgs(new ArrayList<>());
    user.setCreatedOrgs(new ArrayList<>());

    UserDetails userDetails = new UserDetails();
    userDetails.setAvatarURL("");
    userDetails.setAddress("");
    userDetails.setIntroduce("");
    userDetails.setProfession("");
    user.setUserDetails(userDetails);

    return userRepository.save(user);
  }

  @Override
  public UserAccount seekUserByEmail(String email) {
    Optional<UserAccount> user = userRepository.findByEmail(email);
    return user.orElse(null);
  }

  @Override
  public boolean changePassword(String email, String newPassword) {
    Optional<UserAccount> user = userRepository.findByEmail(email);
    if (user.isPresent()) {
      user.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
      userRepository.save(user.get());
      return true;
    }
    return false;
  }

  @Override
  public UserDetails getUserDetailsById(Long userId) {
    Optional<UserAccount> userAccount = userRepository.findById(userId);
    return userAccount.map(UserAccount::getUserDetails).orElse(null);
  }

  @Override
  public UserDetails getUserDetailsByName(String userName) {
    Optional<UserAccount> userAccount = userRepository.findByUserName(userName);
    return userAccount.map(UserAccount::getUserDetails).orElse(null);
  }

  @Override
  public List<Organization> getJoinedOrgsByName(String userName) {
    Optional<UserAccount> user = userRepository.findByUserName(userName);
    return user.map(UserAccount::getJoinedOrgs).orElse(null);
  }

  @Override
  public void sendSmsVerifyCode(String phone) {
    StringBuilder stringBuilder = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 6; i++) {
      stringBuilder.append(random.nextInt(10));
    }

    try {

      Credential cred = new Credential(
          tencentCloudProperties.getSecretId(),
          tencentCloudProperties.getSecretKey()
      );

      HttpProfile httpProfile = new HttpProfile();
      httpProfile.setEndpoint("sms.tencentcloudapi.com");
      ClientProfile clientProfile = new ClientProfile();
      clientProfile.setHttpProfile(httpProfile);
      SmsClient client = new SmsClient(cred, "", clientProfile);
      SendSmsRequest req = new SendSmsRequest();
      String[] phoneNumberSet1 = {"+86" + phone};
      String[] templateParamSet1 = {stringBuilder.toString()};

      req.setPhoneNumberSet(phoneNumberSet1);
      req.setTemplateID("853982");
      req.setTemplateParamSet(templateParamSet1);
      req.setSmsSdkAppid(tencentCloudProperties.getSmsSdkAppId());
      req.setSign("techdictpro");

      SendSmsResponse sendSmsResponse = client.SendSms(req);
      log.info("Send SMS Message Response: " + SendSmsResponse.toJsonString(sendSmsResponse));

      long smsCodeExpires = 300; // 短信验证码 5 分钟过期
      // 验证码并存储到 redis 中
      redisService.set(smsCodePrefix + phone, stringBuilder.toString());
      redisService.expire(smsCodePrefix + phone, smsCodeExpires);
    } catch (TencentCloudSDKException e) {
      throw new CustomException(
          CustomExceptionType.TENCENT_CLOUD_SDK_ERROR,
          "短信发送出现异常，错误信息: \n" + e.toString()
      );
    }
  }

  @Override
  public DUPLICATE_TYPES checkAndNoDuplicate(String userName, String email, String phone) {
    if (userRepository.findByUserName(userName).isPresent()) return DUPLICATE_TYPES.WITH_USERNAME;
    else if (userRepository.findByEmail(email).isPresent()) return DUPLICATE_TYPES.WITH_EMAIL;
    else if (userRepository.findByPhone(phone).isPresent()) return DUPLICATE_TYPES.WITH_PHONE;
    return DUPLICATE_TYPES.PASS;
  }
}
