package com.pegasus.security.sso.cas.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by enHui.Chen on 2021/3/1.
 */
@Service
public class CasUserDetailService implements AuthenticationUserDetailsService<CasUsernamePasswordAuthenticationToken> {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public UserDetails loadUserDetails(CasUsernamePasswordAuthenticationToken token) throws UsernameNotFoundException {
        return userDetailsService.loadUserByUsername(token.getName());
    }
}
