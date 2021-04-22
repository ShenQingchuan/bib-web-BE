package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.*;
import pro.techdict.bib.bibserver.beans.DUPLICATE_TYPES;
import pro.techdict.bib.bibserver.dtos.OrgSimpleDto;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.services.PasswordRetrieveService;
import pro.techdict.bib.bibserver.services.RedisService;
import pro.techdict.bib.bibserver.services.UserService;
import pro.techdict.bib.bibserver.services.impls.UserServiceImpl;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

  final UserService userService;
  final PasswordRetrieveService passwordRetrieveService;
  final RedisService redisService;

  public UserController(UserService userService, PasswordRetrieveService passwordRetrieveService, RedisService redisService) {
    this.userService = userService;
    this.passwordRetrieveService = passwordRetrieveService;
    this.redisService = redisService;
  }

  @PostMapping("/register")
  public HttpResponse registerUser(@RequestBody Map<String, String> requestBody) {
    String userName = requestBody.get("userName");
    String email = requestBody.get("userEmail");
    String password = requestBody.get("password");
    String phone = requestBody.get("userPhone");
    String phoneVerify = requestBody.get("phoneVerify");

    // 避免某账号重复注册
    DUPLICATE_TYPES duplicate_types = userService.checkAndNoDuplicate(userName, email, phone);
    if (duplicate_types != DUPLICATE_TYPES.PASS) {
      return HttpResponse.fail(duplicate_types.toString());
    }

    String cacheCode = redisService.get(UserServiceImpl.smsCodePrefix + phone);
    if (cacheCode == null) {
      throw new CustomException(CustomExceptionType.VERIFY_CODE_NOT_EXISTS);
    }
    if (!cacheCode.equals(phoneVerify)) {
      return HttpResponse.fail("短信验证码输入不正确！");
    } else redisService.remove(UserServiceImpl.smsCodePrefix + phone); // 删除短信验证码缓存

    UserAccount newUser = userService.registerUser(userName, password, phone, email);
    // 屏蔽 password 字段
    HashMap<String, Object> newUserReturn = new HashMap<>();
    newUserReturn.put("uid", newUser.getId());
    newUserReturn.put("userName", newUser.getUserName());
    newUserReturn.put("role", newUser.getRole());
    newUserReturn.put("registerDate", newUser.getCreateTime());
    return HttpResponse.success("注册成功！", newUserReturn);
  }

  @GetMapping("/seekByEmail")
  public HttpResponse seekUserByEmail(@RequestParam(value = "email") String email) {
    UserAccount userAccount = userService.seekUserByEmail(email);
    if (userAccount == null) {
      return HttpResponse.fail("未找到以该邮箱注册的用户！");
    } else {
      Map<String, String> data = new HashMap<>();
      data.put("userName", userAccount.getUserName());
      return HttpResponse.success("找到该用户！", data);
    }
  }

  @PostMapping("/passwordRetrieve")
  public HttpResponse passwordRetrieve(@RequestBody Map<String, String> requestBody) {
    String email = requestBody.get("userEmail");
    try {
      passwordRetrieveService.generateVerifyCodeByEmail(email);
      return HttpResponse.success("已发送验证码到邮箱！");
    } catch (CustomException e) {
      if (e.getType() == CustomExceptionType.TENCENT_CLOUD_SDK_ERROR) {
        return HttpResponse.error(e.getType(), "发送验证码邮件失败！请联系开发团队！");
      }
      return HttpResponse.error(e);
    }
  }

  @PostMapping("/passwordRetrieveVerify")
  public HttpResponse passwordRetrieveVerify(@RequestBody Map<String, String> requestBody) {
    String email = requestBody.get("userEmail");
    String requestCode = requestBody.get("vcode");
    String newPassword = requestBody.get("newPassword");
    if (!passwordRetrieveService.verifyAuthCodeForEmail(email, requestCode)) {
      return HttpResponse.fail("验证码错误，请核对后再试！");
    }
    if (!userService.changePassword(email, newPassword)) {
      return HttpResponse.error(new CustomException(CustomExceptionType.CHANGE_PASSWORD_FAILED));
    }
    return HttpResponse.success("验证通过！");
  }

  @GetMapping("/joinedOrgs")
  public HttpResponse getUsersJoinedOrgs(
      @RequestParam("userName") String userName
  ) {
    List<OrgSimpleDto> joinedOrgs = userService.getJoinedOrgsByName(userName);
    if (joinedOrgs != null) {
      return HttpResponse.success("获取用户所在团队成功！", joinedOrgs);
    } else return HttpResponse.fail("未找到该用户所在团队信息！");
  }

  @PostMapping("/sendSmsCode")
  public HttpResponse sendSmsCode(@RequestBody Map<String, String> requestBody) {
    String phone = requestBody.get("userPhone");
    try {
      userService.sendSmsVerifyCode(phone);
      return HttpResponse.success("验证码发送成功");
    } catch (CustomException e) {
      return HttpResponse.error(e);
    }
  }
}
