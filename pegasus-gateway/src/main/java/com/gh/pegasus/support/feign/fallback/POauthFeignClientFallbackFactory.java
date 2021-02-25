package com.gh.pegasus.support.feign.fallback;

import com.gh.pegasus.support.feign.POauthFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/10/26.
 */
@Slf4j
public class POauthFeignClientFallbackFactory implements FallbackFactory<POauthFeignClient> {
    @Override
    public POauthFeignClient create(Throwable throwable) {
        return new POauthFeignClient() {

            @Override
            public Map<String, Object> user(Principal principal, String Token) {
                return null;
            }
        };
    }
}
