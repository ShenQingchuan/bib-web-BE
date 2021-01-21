package pro.techdict.bib.bibserver.services;

public interface PasswordRetrieveService {
  /**
   * 为用户的找回密码请求生成一个验证码存入 Redis 并发送到用户邮箱地址
   * @param email 用户邮箱地址
   */
  void generateVerifyCodeByEmail(String email);

  /**
   *
   * @param email 作为 Redis 查询的 key 的邮箱地址
   * @param requestCode 请求中用户输入的验证码
   * @return 返回是否验证正确
   */
  boolean verifyAuthCodeForEmail(String email, String requestCode);
}
