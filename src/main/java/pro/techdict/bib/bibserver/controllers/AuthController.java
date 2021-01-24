package pro.techdict.bib.bibserver.controllers;

import org.springframework.web.bind.annotation.*;
import pro.techdict.bib.bibserver.entities.UserAccount;
import pro.techdict.bib.bibserver.exceptions.CustomException;
import pro.techdict.bib.bibserver.exceptions.CustomExceptionType;
import pro.techdict.bib.bibserver.services.PasswordRetrieveService;
import pro.techdict.bib.bibserver.services.impls.UserAuthServiceImpl;
import pro.techdict.bib.bibserver.utils.HttpResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

  final UserAuthServiceImpl userAuthService;
  final PasswordRetrieveService passwordRetrieveService;

  public AuthController(UserAuthServiceImpl userAuthService, PasswordRetrieveService passwordRetrieveService) {
    this.userAuthService = userAuthService;
    this.passwordRetrieveService = passwordRetrieveService;
  }

  @PostMapping("/register")
  public HttpResponse registerUser(@RequestBody Map<String, String> requestBody) {
    String userName = requestBody.get("userName");
    String email = requestBody.get("userEmail");
    String password = requestBody.get("password");
    String phone = requestBody.get("phone");
    UserAccount newUser = userAuthService.registerUser(userName, password, phone, email);
    // 屏蔽 password 字段
    HashMap<String, Object> newUserReturn = new HashMap<>();
    newUserReturn.put("uid", newUser.getUid());
    newUserReturn.put("userName", newUser.getUserName());
    newUserReturn.put("role", newUser.getRole());
    newUserReturn.put("registerDate", newUser.getRegisterDate());
    return HttpResponse.success("注册成功！", newUserReturn);
  }

  @GetMapping("/seekByEmail")
  public HttpResponse seekUserByEmail(@RequestParam(value = "email") String email) {
    UserAccount userAccount = userAuthService.seekUserByEmail(email);
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
      return HttpResponse.error(e.getType(), "发送验证码邮件失败！请联系开发团队！");
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
    if (!userAuthService.changePassword(email, newPassword)) {
      return HttpResponse.error(new CustomException(CustomExceptionType.CHANGE_PASSWORD_FAILED));
    }
    return HttpResponse.success("验证通过！");
  }

}
