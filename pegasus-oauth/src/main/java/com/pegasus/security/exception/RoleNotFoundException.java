package com.pegasus.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
public class RoleNotFoundException extends AuthenticationException {
    public RoleNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
