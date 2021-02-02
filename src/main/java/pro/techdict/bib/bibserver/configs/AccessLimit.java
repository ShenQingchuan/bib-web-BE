package pro.techdict.bib.bibserver.configs;


import java.lang.annotation.*;

/**
 * @author: ShenQingchuan
 * @Description: 限流自定义注解
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
  /**
   * 指定 second 时间内，API 最多的请求次数
   */
  int times() default 3;

  /**
   * 指定时间 second，redis 数据过期时间
   */
  int second() default 10;
}
