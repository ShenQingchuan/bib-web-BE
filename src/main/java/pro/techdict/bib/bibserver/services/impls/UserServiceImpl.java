package pro.techdict.bib.bibserver.services.impls;

import com.qcloud.cos.utils.StringUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.beans.DUPLICATE_TYPES;
import pro.techdict.bib.bibserver.configs.JWTProperties;
import pro.techdict.bib.bibserver.configs.TencentCloudProperties;
import pro.techdict.bib.bibserver.daos.UserActivityRepository;
import pro.techdict.bib.bibserver.daos.UserDetailsRepository;
import pro.techdict.bib.bibserver.daos.UserRepository;
import pro.techdict.bib.bibserver.dtos.OrgSimpleDto;
import pro.techdict.bib.bibserver.dtos.UserDetailsFullDto;
import pro.techdict.bib.bibserver.dtos.UserDetailsSimpleDto;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.entities.UserDetails;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.services.RedisService;
import pro.techdict.bib.bibserver.services.UserService;
import pro.techdict.bib.bibserver.utils.JWTUtils;
import reactor.util.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
  private final RedisService redisService;
  private final UserRepository userRepository;
  private final UserDetailsRepository detailsRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final TencentCloudProperties tencentCloudProperties;
  private final UserActivityRepository userActivityRepository;
  private final JWTProperties jwtProperties;

  public final static String smsCodePrefix = "SMSCode:+86";

  public UserServiceImpl(
      RedisService redisService, BCryptPasswordEncoder bCryptPasswordEncoder,
      UserRepository userRepository,
      UserDetailsRepository detailsRepository,
      TencentCloudProperties tencentCloudProperties,
      UserActivityRepository userActivityRepository, JWTProperties jwtProperties) {
    this.redisService = redisService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userRepository = userRepository;
    this.detailsRepository = detailsRepository;
    this.tencentCloudProperties = tencentCloudProperties;
    this.userActivityRepository = userActivityRepository;
    this.jwtProperties = jwtProperties;
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

  private UserDetailsFullDto getUserDetailsFullDto(@Nullable Long readerId, Optional<UserAccount> user) {
    if (user.isPresent()) {
      UserDetailsFullDto detailsFullDto = UserDetailsFullDto.fromEntity(
          user.get().getUserDetails()).setFAndSCount(user.get()
      );

      if (readerId != null) {
        Optional<UserAccount> reader = userRepository.findById(readerId);
        if (reader.isPresent()) {
          detailsFullDto.setIsFollowing(
              user.get().getFollowers().contains(reader.get())
          );
        } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
      }

      return detailsFullDto;

    } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
  }
  @Override
  public UserDetailsFullDto getUserDetailsById(Long userId, @Nullable Long readerId) {
    Optional<UserAccount> user = userRepository.findById(userId);
    return getUserDetailsFullDto(readerId, user);
  }
  @Override
  public UserDetailsFullDto getUserDetailsByName(String userName, @Nullable Long readerId) {
    Optional<UserAccount> user = userRepository.findByUserName(userName);
    return getUserDetailsFullDto(readerId, user);
  }

  @Override
  public UserDetailsSimpleDto updateUserDetails(Map<String, String> newDetailsData) {
    Long uid = Long.parseLong(newDetailsData.get("userId"));
    Optional<UserAccount> user = userRepository.findById(uid);
    if (user.isPresent()) {
      UserDetails details = user.get().getUserDetails();
      String address = newDetailsData.get("address"),
          avatarURL = newDetailsData.get("avatarURL"),
          introduce = newDetailsData.get("introduce"),
          profession = newDetailsData.get("profession");
      if (!StringUtils.isNullOrEmpty(address)) { details.setAddress(address); }
      if (!StringUtils.isNullOrEmpty(introduce)) { details.setIntroduce(introduce); }
      if (!StringUtils.isNullOrEmpty(profession)) { details.setProfession(profession); }
      if (!StringUtils.isNullOrEmpty(avatarURL)) {
        if (!details.getAvatarURL().equals(avatarURL)) details.setAvatarURL(avatarURL);
      }
      return UserDetailsSimpleDto.fromEntity(detailsRepository.save(details));
    } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
  }

  @Override
  public String updateAvatarURLForToken(String originToken, String avatarURL) {
    Claims claims = JWTUtils.parserToken(originToken.substring(7), jwtProperties);
    if (claims != null) {
      claims.put("avatarURL", avatarURL);
      Optional<UserAccount> user = userRepository.findByUserName(claims.getSubject());
      if (user.isPresent()) {
        user.get().getUserDetails().setAvatarURL(avatarURL);
        userRepository.save(user.get());
      } else throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
      return Jwts.builder()
          .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
          .addClaims(claims).compact();
    } else throw new CustomException(CustomExceptionType.TOKEN_PARSE_ERROR);
  }

  @Override
  public List<OrgSimpleDto> getJoinedOrgsByName(String userName) {
    Optional<UserAccount> user = userRepository.findByUserName(userName);
    return user.map(userAccount -> userAccount.getJoinedOrgs().stream()
        .map(OrgSimpleDto::fromEntity)
        .collect(Collectors.toList())).orElse(null);
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
          "短信发送出现异常，错误信息: \n" + e
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

  @Override
  public boolean triggerFollowUser(Long srcUid, String targetUserName) {
    Optional<UserAccount> srcUser = userRepository.findById(srcUid);
    if (srcUser.isPresent()) {
      Optional<UserAccount> targetUser = userRepository.findByUserName(targetUserName);
      if (targetUser.isPresent()) {
        boolean hasUnFollowed = targetUser.get().getFollowers()
            .removeIf(user -> user.getId().equals(srcUid));
        if (!hasUnFollowed) {
          targetUser.get().getFollowers().add(srcUser.get());
        }
        UserAccount savedTargetUser = userRepository.save(targetUser.get());

        /*
         TODO 启动 redis 定时任务：当中 如果没有 1分钟内 立即取消关注则发送 动态时间
            UserActivity focusUserActivity = new UserActivity();
            focusUserActivity.setActivityType(USER_ACTIVITY_TYPES.FOCUS_USER);
            focusUserActivity.setCreator(srcUser.get());
            focusUserActivity.setFocusedUser(savedTargetUser);
            userActivityRepository.save(focusUserActivity);
        */

        return true;
      } throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
    } throw new CustomException(CustomExceptionType.USER_NOT_FOUND_ERROR);
  }
}
