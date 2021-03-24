package com.gh.pegasus.security;

import com.gh.pegasus.domain.User;
import com.gh.pegasus.exception.UserNotFoundException;
import com.gh.pegasus.service.IUserDetailsService;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * @author enhui.chen
 * @desc 自定义认证器
 * @date 2021-02-26 23:01:15
 */
public class CustomAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {
    @Autowired
    private IUserDetailsService userDetailsService;

    /**
     * @param name
     * @param servicesManager
     * @param principalFactory
     * @param order
     * @return
     * @throws
     * @desc 自定义认证器
     * @date 2021-02-26 23:06:04
     */
    public CustomAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    /**
     * @param credential
     * @return org.apereo.cas.authentication.HandlerResult
     * @throws
     * @desc 认证核心逻辑
     * @date 2021-02-26 23:06:21
     */
    @Override
    protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        UsernamePasswordSubSystemCredential upssc = (UsernamePasswordSubSystemCredential) credential;
        String username = upssc.getUsername();

        User user = userDetailsService.authenticate(upssc);
        if (user == null) {
            throw new UserNotFoundException();
//            throw new FailedLoginException();
        }

//        user

        // 认证成功
        return createHandlerResult(upssc, this.principalFactory.createPrincipal(username, Collections.emptyMap()), null);
    }

    /**
     * @param credential
     * @return boolean
     * @throws
     * @desc 针对于何种credential开启认证
     * @date 2021-02-26 23:06:35
     */
    @Override
    public boolean supports(Credential credential) {
        return credential instanceof UsernamePasswordSubSystemCredential;
    }
}
