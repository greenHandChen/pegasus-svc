package com.pegasus.security.processors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author enhui.chen
 * @desc security登录处理器
 * @date 2021-03-16 23:12:22
 */
public interface SecurityProcessor<T> {

    T process(HttpServletRequest request, HttpServletResponse response);


    int getOrder();
}
