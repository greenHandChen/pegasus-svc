package com.pegasus.security.resource;

import com.pegasus.security.resource.impl.FullStackResourceMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by enHui.Chen on 2019/9/4.
 */
public interface CuxRequestMatcher extends RequestMatcher {
    FullStackResourceMatcher setAntPathRequestMatchers(AntPathRequestMatcher antPathRequestMatchers);
}
