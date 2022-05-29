package com.xjh.library.common.cache;

import java.time.Duration;

public interface RedisService {
    /**
     * 根据key获取value的值
     * @param key redis的key值
     * @param clazz 返回对象的类型
     * @return 返回指定类型的变量
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 设置key-value，并设置过期时间
     * @param key redis的key的值
     * @param value 需要设置的对象
     * @param expire 过期时间
     */
    void set(String key, Object value, Duration expire);

    /**
     * 删除一个key
     * @param key 待删除的key
     * @return 是否删除成功
     */
    Boolean del(String key);
}
