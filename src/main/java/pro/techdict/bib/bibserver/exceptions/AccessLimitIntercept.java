package pro.techdict.bib.bibserver.exceptions;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pro.techdict.bib.bibserver.configs.AccessLimit;
import pro.techdict.bib.bibserver.utils.HttpResponse;
import pro.techdict.bib.bibserver.utils.IPUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitIntercept implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(AccessLimitIntercept.class);

  @Autowired
  private StringRedisTemplate redisTemplate;

  /**
   * 接口调用前检查对方ip是否频繁调用接口
   * @param request 请求
   * @param response 请求返回值
   * @param handler 处理器
   * @return 判断方法是否有 AccessLimit 注解，没有的返回 true 放行
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    try {
      // handler是否为 HandleMethod 实例
      if (handler instanceof HandlerMethod) {
        // 强转
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取方法
        Method method = handlerMethod.getMethod();

        if (!method.isAnnotationPresent(AccessLimit.class)) {
          return true;
        }

        // 获取注解上的内容
        AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
        if (accessLimit == null) {
          return true;
        }
        // 获取方法注解上的请求次数
        int times = accessLimit.times();
        // 获取方法注解上的请求时间
        int second = accessLimit.second();

        // 拼接redis key = IP + Api限流
        String key = IPUtils.getIpAddr(request) + request.getRequestURI();

        // 获取redis的value
        Integer maxTimes = null;
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasLength(value)) {
          maxTimes = Integer.valueOf(value);
        }
        if (maxTimes == null) {
          // 如果 redis 中没有该 ip 对应的时间则表示第一次调用，保存 key 到 redis
          redisTemplate.opsForValue().set(key, "1", second, TimeUnit.SECONDS);
        } else if (maxTimes < times) {
          // 如果 redis 中的时间比注解上的时间小则表示可以允许访问,这是修改 redis 的 value 时间
          redisTemplate.opsForValue().set(key, maxTimes + 1 + "", second, TimeUnit.SECONDS);
        } else {
          // 请求过于频繁
          logger.info("IP 地址 " + key + " 请求过于频繁");
          return setResponse(HttpResponse.fail("请求过于频繁，请稍后再试！"), response);
        }
      }
    } catch (Exception e) {
      logger.error("API 请求限流拦截异常，异常原因：", e);
      throw new CustomException(CustomExceptionType.ACCESS_LIMIT_ERROR);
    }
    return true;
  }

  private boolean setResponse(HttpResponse returns, HttpServletResponse response) {
    try {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(new Gson().toJson(returns));
    } catch (Exception e) {
      logger.error("AccessLimit setResponse 方法报错", e);
      return false;
    }
    return true;
  }
}

