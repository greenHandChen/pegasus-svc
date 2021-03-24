package com.pegasus.common.redis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by enHui.Chen on 2021/3/23.
 */
public interface RedisManager {

    /**
     * @param key
     * @param value
     * @return
     * @date 2021-03-23 15:33:34
     * @desc string类型根据key获取value
     **/
    void strSet(String key, String value);

    /**
     * @param key
     * @return
     * @date 2021-03-23 15:33:14
     * @desc string类型根据key获取value
     **/
    String strGet(String key);

    /**
     * @param key
     * @param value
     * @param expire
     * @param timeUnit
     * @return
     * @date 2021-03-23 15:32:50
     * @desc string类型根据key+expire设置value
     **/
    void strSet(String key, String value, long expire, TimeUnit timeUnit);

    /**
     * @param key
     * @param hashKey
     * @param value
     * @return
     * @date 2021-03-23 15:32:21
     * @desc hash类型根据key+hashKey设置value
     **/
    void hSet(String key, String hashKey, String value);

    /**
     * @param key
     * @param values
     * @return
     * @date 2021-03-23 15:30:35
     * @desc hash类型根据key设置values
     **/
    void hSetAll(String key, Map<String, String> values);

    /**
     * @param key
     * @param hashKey
     * @return
     * @date 2021-03-23 15:30:06
     * @desc hash类型根据key+hashKey获取value
     **/
    String hGet(String key, String hashKey);

    /**
     * @param key
     * @return
     * @date 2021-03-23 15:28:50
     * @desc hash类型根据key获取所有values
     **/
    Map<String, String> hGetAll(String key);

    /**
     * @param key
     * @return
     * @date 2021-03-23 15:30:06
     * @desc hash类型根据key+hashKeys删除values
     **/
    Long hDelete(String key, Object... hashKeys);

    /**
     * @param key
     * @param expire
     * @param timeUnit
     * @return
     * @date 2021-03-23 15:25:55
     * @desc 设置key的过期时间
     **/
    void setExpire(String key, long expire, TimeUnit timeUnit);

    /**
     * @param key
     * @return
     * @date 2021-03-23 15:44:25
     * @desc 根据key删除value
     **/
    Boolean delByKey(String key);
}
