package com.gh.pegasus.support.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Created by enHui.Chen on 2020/10/22.
 */
public class ServerRequestUtil {

    public static String getHeaderByName(ServerHttpRequest request, String name) {
        String value = request.getHeaders().getFirst(name);
        return StringUtils.isEmpty(value) ? "" : value;
    }
}
