package pro.techdict.bib.bibserver.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import pro.techdict.bib.bibserver.configs.JWTProperties;
import pro.techdict.bib.bibserver.utils.HttpResponse;
import pro.techdict.bib.bibserver.utils.JWTUserDetails;
import pro.techdict.bib.bibserver.utils.JWTUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private byte[] requestInputStreamBytes;
  JWTProperties jwtProperties;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTProperties jwtProperties) {
    this.authenticationManager = authenticationManager;
    this.jwtProperties = jwtProperties;
    super.setFilterProcessesUrl("/user/login");
  }

  @Data
  static class UserNameLoginFormModel {
    String userName;
    String password;
    int formType;
    boolean rememberMe;
  }

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws AuthenticationException {
    ServletInputStream stream = request.getInputStream();
    requestInputStreamBytes = StreamUtils.copyToString(stream, StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8);
    UserNameLoginFormModel form = new ObjectMapper().readValue(requestInputStreamBytes, UserNameLoginFormModel.class);
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(form.getUserName(), form.getPassword(), new ArrayList<>())
    );
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication authResult) throws IOException {

    // 调用 getPrincipal() 方法会返回一个实现了 `org.springframework.security.core.userdetails.UserDetails` 接口的对象，
    // 实现中以 `JWTUserDetails` 继承
    JWTUserDetails jwtUserDetails = (JWTUserDetails) authResult.getPrincipal();
    UserNameLoginFormModel form = new ObjectMapper().readValue(requestInputStreamBytes, UserNameLoginFormModel.class);
    String token = JWTUtils.makeJsonWebToken(jwtUserDetails, form.rememberMe, jwtProperties);
    // 按规定格式返回创建成功的 token
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    Map<String, String> data = new HashMap<>();
    data.put("token", token);
    response.getWriter().write(
        new Gson().toJson(HttpResponse.success("登录成功", data))
    );
  }

  // 验证失败，密码错误时候调用的方法
  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed
  ) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(
        new Gson().toJson(HttpResponse.fail("登录失败，密码错误！"))
    );
  }

}
