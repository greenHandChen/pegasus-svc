package com.gh.pegasus.support.feign;

import com.gh.pegasus.support.feign.fallback.POauthFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/10/26.
 */
@FeignClient(
        value = "pegasus-oauth",
        fallbackFactory = POauthFeignClientFallbackFactory.class,
        path = "/oauth/api"
)
public interface POauthFeignClient {

    @GetMapping("/oauth/user")
    Map<String,Object> user(@RequestParam Principal principal, @RequestHeader(name = "Authorization") String Token);
}
