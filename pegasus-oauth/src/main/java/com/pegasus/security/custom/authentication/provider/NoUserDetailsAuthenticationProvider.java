package com.pegasus.security.custom.authentication.provider;

import com.pegasus.security.custom.authentication.provider.dao.UsernameNoPasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class NoUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    public boolean supports(Class<?> authentication) {
        return UsernameNoPasswordAuthenticationToken.class.getTypeName().equals(authentication.getTypeName());
    }

    public UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            } else {
                return loadedUser;
            }
        } catch (Exception e) {

        }
        return null;
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
