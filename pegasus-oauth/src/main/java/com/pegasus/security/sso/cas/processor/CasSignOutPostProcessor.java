package com.pegasus.security.sso.cas.processor;

import com.pegasus.common.redis.RedisHelper;
import com.pegasus.security.processors.SecurityProcessor;
import com.pegasus.security.sso.cas.constants.CasConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author enhui.chen
 * @desc cas登出processor
 * @date 2021-03-24 16:39:32
 */
@Component
public class CasSignOutPostProcessor implements SecurityProcessor {
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private TokenStore tokenStore;

    /**
     * @param request
     * @param response
     * @param ticket
     * @return
     * @date 2021-03-24 17:52:35
     * @desc 销毁ticket/access_token
     **/
    public Object process(HttpServletRequest request, HttpServletResponse response, String ticket) {
        if (!StringUtils.isEmpty(ticket)) {
            // 销毁ticket/access_token
            String accessToken = redisHelper.strGet(CasConstant.SSO_CAS_TICKET_TOKEN + ticket);
            redisHelper.delByKey(CasConstant.SSO_CAS_TICKET_TOKEN + ticket);
            redisHelper.delByKey(CasConstant.SSO_CAS_TOKEN_TICKET + accessToken);

            // 注销access_token/refresh_token
            tokenStore.removeAccessToken(new DefaultOAuth2AccessToken(accessToken));
        }
        return process(request, response);
    }

    /**
     * @param request
     * @param response
     * @return
     * @date 2021-03-24 16:42:53
     * @desc 销毁ticket/access_token
     **/
    @Override
    public Object process(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
