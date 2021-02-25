package com.pegasus.filter;

import com.pegasus.security.utils.SecurityUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by enHui.Chen on 2019/8/28.
 */
@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        SecurityUtil.setCorsHeader((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
