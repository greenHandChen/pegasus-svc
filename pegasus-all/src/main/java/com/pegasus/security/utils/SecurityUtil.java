package com.pegasus.security.utils;

import com.pegasus.common.utils.ApplicationContextUtil;
import com.pegasus.security.constant.Constant;
import com.pegasus.security.dto.CuxUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
public class SecurityUtil {
    /**
     * @Author: enHui.Chen
     * @Description: 获取当前用户信息
     * @Data 2019/9/5
     */
    public static CuxUserDetails getCurrentUser() {
        Authentication authentication;
        if (SecurityContextHolder.getContext() == null || (authentication = SecurityContextHolder.getContext().getAuthentication()) == null) {
            throw new UsernameNotFoundException("当前用户未登录!");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CuxUserDetails) {
            return (CuxUserDetails) principal;
        }

        Object details = authentication.getDetails();
        if (details instanceof OAuth2AuthenticationDetails) {
            Object decodeDetails = ((OAuth2AuthenticationDetails) details).getDecodedDetails();
            if (decodeDetails instanceof CuxUserDetails) {
                return (CuxUserDetails) decodeDetails;
            }
        }

        throw new UsernameNotFoundException("当前用户未登录!");
    }

    /**
     * @Author: enHui.Chen
     * @Description: BCrypt加密(加盐加密)
     * @Data 2019/10/18
     */
    public static String BCryptEncode(String rawPassword) {
        PasswordEncoder passwordEncoder = ApplicationContextUtil.getApplicationContext().getBean("passwordEncoder", PasswordEncoder.class);
        String hashPassword = passwordEncoder.encode(rawPassword);
        return hashPassword;
    }

    /**
     * @Author: enHui.Chen
     * @Description: BCrypt数据比较(原数据, 加密数据)
     * @Data 2019/10/18
     */
    public static Boolean BCryptMatch(String rawPassword, String hashPassword) {
        PasswordEncoder passwordEncoder = ApplicationContextUtil.getApplicationContext().getBean("passwordEncoder", PasswordEncoder.class);
        return passwordEncoder.matches(rawPassword, hashPassword);
    }
}
