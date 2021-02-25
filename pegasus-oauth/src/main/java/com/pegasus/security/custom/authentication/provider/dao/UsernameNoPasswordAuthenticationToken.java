package com.pegasus.security.custom.authentication.provider.dao;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * An {@link org.springframework.security.core.Authentication} implementation that is
 * designed for simple presentation of a username and password.
 * <p>
 * The <code>principal</code> and <code>credentials</code> should be set with an
 * <code>Object</code> that provides the respective property via its
 * <code>Object.toString()</code> method. The simplest such <code>Object</code> to use is
 * <code>String</code>.
 *
 * @author Ben Alex
 */
public class UsernameNoPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public UsernameNoPasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UsernameNoPasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
