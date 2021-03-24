package com.pegasus.security.sso.cas.processor;

import com.pegasus.common.redis.RedisHelper;
import com.pegasus.security.processors.SecurityProcessor;
import com.pegasus.security.sso.cas.constants.CasConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author enhui.chen
 * @desc cas登录processor
 * @date 2021-03-24 16:39:32
 */
@Component
public class CasSignInPostProcessor implements SecurityProcessor {

    /**
     * @param request
     * @param response
     * @return
     * @date 2021-03-24 16:40:16
     * @desc 保存记录ticket
     **/
    @Override
    public Object process(HttpServletRequest request, HttpServletResponse response) {
        String casTicket = request.getParameter("ticket");
        request.getSession(false).setAttribute(CasConstant.SSO_CAS_TICKET, casTicket);
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
