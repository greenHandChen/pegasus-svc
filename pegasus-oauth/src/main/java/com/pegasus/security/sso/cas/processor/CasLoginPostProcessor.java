package com.pegasus.security.sso.cas.processor;

import com.pegasus.common.redis.RedisHelper;
import com.pegasus.security.processors.oauth2.AuthorizationPostProcessor;
import com.pegasus.security.sso.cas.constants.CasConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author enhui.chen
 * @desc CAS登录成功处理器
 * @date 2021-03-16 23:28:06
 */
@Slf4j
@Component
public class CasLoginPostProcessor implements AuthorizationPostProcessor {
    @Autowired
    private RedisHelper redisHelper;

    @Override
    public Object process(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    /**
     * @param request
     * @param response
     * @param accessToken
     * @return
     * @date 2021-03-16 23:29:18
     * @desc 登录成功后生成ticket, 将ticket与token绑定, 用于单点登出销毁
     **/
    @Override
    public Object process(HttpServletRequest request, HttpServletResponse response, OAuth2AccessToken accessToken) {
        String casTicket = (String) request.getSession(false).getAttribute(CasConstant.SSO_CAS_TICKET);
        String token = accessToken.getValue();
        if (log.isDebugEnabled()) {
            log.debug("casTicket is :{}, access_token is :{}", casTicket, token);
        }
        // ticket-token
        redisHelper.strSet(CasConstant.SSO_CAS_TICKET_TOKEN + casTicket, token, 24, TimeUnit.HOURS);
        // token-ticket
        redisHelper.strSet(CasConstant.SSO_CAS_TOKEN_TICKET + token, casTicket, 24, TimeUnit.HOURS);

        return process(request, response);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
