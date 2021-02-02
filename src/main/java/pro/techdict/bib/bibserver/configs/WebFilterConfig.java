package pro.techdict.bib.bibserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pro.techdict.bib.bibserver.exceptions.AccessLimitIntercept;

@Configuration
public class WebFilterConfig implements WebMvcConfigurer {

  /**
   * 这里需要先将限流拦截器入住，不然无法获取到拦截器中的 redisTemplate
   */
  @Bean
  public AccessLimitIntercept getAccessLimitIntercept() {
    return new AccessLimitIntercept();
  }

  /**
   * 多个拦截器组成一个拦截器链
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getAccessLimitIntercept()).addPathPatterns("/**");
  }

}
