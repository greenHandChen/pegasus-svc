package com.pegasus.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by enHui.Chen on 2019/9/6.
 */
public class NotLoginException extends AuthenticationException {
    public NotLoginException(String msg, Throwable t) {
        super(msg, t);
    }

    public NotLoginException(String msg) {
        super(msg);
    }
}
