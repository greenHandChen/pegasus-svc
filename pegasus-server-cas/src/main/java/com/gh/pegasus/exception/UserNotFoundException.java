package com.gh.pegasus.exception;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountNotFoundException;

/**
 * Created by enHui.Chen on 2021/2/27.
 */
public class UserNotFoundException extends AccountNotFoundException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String arg0) {
        super(arg0);
    }
}
