package com.pigbox.ddd.infrastructure.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;

public interface RedisInfrasService {

    void setString(String key, String value);
    String getString(String key);

    void setObject(String key, Object value);
    void setObjectWithTtl(String key, Object value, String ttl);

    <T> T getOject(String key, Class<T> targetClass);

    void delete(String key);

    void setInt(String key, Integer value);
    Integer getInt(String key);

    RedisTemplate<String, Object> getRedisTemplate();
}
