package com.pegasus.security.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by enHui.Chen on 2019/11/26.
 */
public class Constant {
    public static final Set<String> ALLOW_ORIGIN = new HashSet<>();

    public static final String PE_OAUTH = "poauth";

    // 跨域可访问主机列表
    static {
        ALLOW_ORIGIN.add("http://localhost");
        ALLOW_ORIGIN.add("http://localhost:80");
        ALLOW_ORIGIN.add("http://localhost:8000");

        ALLOW_ORIGIN.add("http://192.168.209.135");
        ALLOW_ORIGIN.add("http://192.168.209.135:80");
        ALLOW_ORIGIN.add("http://192.168.209.135:8000");

        ALLOW_ORIGIN.add("http://192.168.91.1");
        ALLOW_ORIGIN.add("http://192.168.91.1:80");
        ALLOW_ORIGIN.add("http://192.168.91.1:8000");

        ALLOW_ORIGIN.add("http://192.168.91.131");
        ALLOW_ORIGIN.add("http://192.168.91.131:80");
        ALLOW_ORIGIN.add("http://192.168.91.131:8000");
    }
}
