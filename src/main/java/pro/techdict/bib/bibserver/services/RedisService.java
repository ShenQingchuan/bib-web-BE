package pro.techdict.bib.bibserver.services;

public interface RedisService {
  /**
   * 存储数据
   */
  void set(String key, String value);

  /**
   * 获取数据
   */
  String get(String key);

  /**
   * 设置超期时间
   */
  void expire(String key, long expire);

  /**
   * 删除数据
   */
  void remove(String key);

  /**
   * 是否过期
   */
  boolean isExpired(String key);
}
