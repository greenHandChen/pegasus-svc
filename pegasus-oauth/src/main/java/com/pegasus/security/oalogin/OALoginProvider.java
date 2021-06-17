package com.pegasus.security.oalogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Created by enHui.Chen on 2021/6/8.
 */
@Component
public class OALoginProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            UserDetails loadedUser = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            } else {
                return  createSuccessAuthentication(authentication, loadedUser);
            }
        } catch (Exception e) {

        }
        return null;
    }

    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        OALoginToken result = new OALoginToken(user.getUsername(), null, user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    public boolean supports(Class<?> authentication) {
        return OALoginToken.class.getTypeName().equals(authentication.getTypeName());
    }

}
