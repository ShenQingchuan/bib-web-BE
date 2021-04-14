package pro.techdict.bib.bibserver.services.impls;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import pro.techdict.bib.bibserver.services.RedisService;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
  private final StringRedisTemplate stringRedisTemplate;

  public RedisServiceImpl(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  /**
   * 设置一个 key-value 键值对
   * @param key 键
   * @param value 值
   */
  @Override
  public void set(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  /**
   * 根据 key 获取值
   * @param key 键值
   */
  @Override
  public String get(String key) {
    return stringRedisTemplate.opsForValue().get(key);
  }

  /**
   * 设置超期时间
   * @param key 键值
   * @param expire 过期时间（秒）
   */
  @Override
  public void expire(String key, long expire) {
    stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
  }

  /**
   * 删除 key
   * @param key 键值
   */
  @Override
  public void remove(String key) {
    stringRedisTemplate.delete(key);
  }

  @Override
  public boolean isExpired(String key) {
    Long remainSeconds = stringRedisTemplate.opsForValue().getOperations().getExpire(key);
    return remainSeconds == null || remainSeconds < 1;
  }
}
