package com.xjh.library.common.cache.impl;

import com.xjh.library.common.cache.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null) return null;
        return clazz.cast(value);
    }

    @Override
    public void set(String key, Object value, Duration expire) {
        redisTemplate.opsForValue().set(key,value,expire);
    }


    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }
}
