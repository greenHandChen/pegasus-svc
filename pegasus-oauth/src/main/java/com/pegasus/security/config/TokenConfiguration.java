package com.pegasus.security.config;

import com.pegasus.security.custom.CustomRedisTokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by enHui.Chen on 2020/9/24.
 */
@Configuration
public class TokenConfiguration {

    /**
     * @Author: enHui.Chen
     * @Description: 自定义客户端校验策略
     * @Data 2020/9/24
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义令牌存储策略
     * @Data 2020/9/24
     */
    @Bean
    public TokenStore customRedisTokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new CustomRedisTokenStore(redisConnectionFactory);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 自定义令牌服务
     * @Data 2020/9/24
     */
/*    @Bean
    @ConditionalOnMissingBean(value = {AuthorizationServerTokenServices.class,DefaultTokenServices.class})
    public AuthorizationServerTokenServices tokenServices(ClientDetailsService jdbcClientDetailsService, TokenStore tokenStore) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);// 支持令牌刷新
        defaultTokenServices.setAccessTokenValiditySeconds(7200);// 设置令牌有效期2小时
        defaultTokenServices.setRefreshTokenValiditySeconds(14400);// 刷新令牌有效期4小时
        defaultTokenServices.setClientDetailsService(jdbcClientDetailsService);// 设置客户端信息
        defaultTokenServices.setTokenStore(tokenStore);// 设置令牌存储策略
        return defaultTokenServices;
    }*/
}
