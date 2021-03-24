package com.pegasus.common.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by enHui.Chen on 2021/3/23.
 */
@Component
public class RedisHelper implements InitializingBean, RedisManager {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOpr;

    private HashOperations<String, String, String> hashOpr;


    @Override
    public void afterPropertiesSet() {
        Assert.notNull(redisTemplate, "redisTemplate cant't be null");
        this.valueOpr = redisTemplate.opsForValue();
        this.hashOpr = redisTemplate.opsForHash();
    }


    @Override
    public void strSet(String key, String value) {
        valueOpr.set(key, value);
    }

    @Override
    public String strGet(String key) {
        return valueOpr.get(key);
    }

    @Override
    public void strSet(String key, String value, long expire, TimeUnit timeUnit) {
        this.strSet(key, value);
        if (expire != -1) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }
    }

    @Override
    public void hSet(String key, String hashKey, String value) {
        hashOpr.put(key, hashKey, value);
    }

    @Override
    public void hSetAll(String key, Map<String, String> values) {
        hashOpr.putAll(key, values);
    }

    @Override
    public String hGet(String key, String hashKey) {
        return hashOpr.get(key, hashKey);
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        return hashOpr.entries(key);
    }

    @Override
    public Long hDelete(String key, Object... hashKeys) {
        return hashOpr.delete(key, hashKeys);
    }


    @Override
    public void setExpire(String key, long expire, TimeUnit timeUnit) {
        redisTemplate.expire(key, expire, timeUnit);
    }

    @Override
    public Boolean delByKey(String key) {
        return redisTemplate.delete(key);
    }
}
