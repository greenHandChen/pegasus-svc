package com.gh.pegasus.security;

import org.apereo.cas.authentication.RememberMeUsernamePasswordCredential;

/**
 * @author enhui.chen
 * @desc 扩展登录信息
 * @date 2021-02-26 23:15:47
 */
public class UsernamePasswordSubSystemCredential extends RememberMeUsernamePasswordCredential {
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
