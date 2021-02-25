package com.pegasus.security.custom;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Created by enHui.Chen on 2020/9/24.
 */
public class CustomRedisTokenStore extends RedisTokenStore {
    public CustomRedisTokenStore(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }
}
