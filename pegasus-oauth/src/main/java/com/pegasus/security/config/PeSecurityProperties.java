package com.pegasus.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by enHui.Chen on 2021/3/1.
 */
@Data
@ConfigurationProperties(prefix = PeSecurityProperties.PREFIX)
public class PeSecurityProperties {
    public static final String PREFIX = "pegasus.oauth";
    private String defaultUrl;

    // 登录配置
    private Login login = new Login();

    // 登出配置
    private Logout logout = new Logout();

    // 单点配置
    private SSO sso = new SSO();

    @Data
    public static class Login {
        private String page = "/login";
    }

    @Data
    public static class Logout {
        private String page = "/logout";
    }

    @Data
    public static class SSO {
        // 是否启用sso
        private boolean enabled = false;

        // 服务端host
        private String serverUrl = "http://localhost:9090/cas";

        // 服务端login
        private String serverLogin = "http://localhost:9090/cas/login";

        // 服务端logout
        private String serverLogout = "http://localhost:9090/cas/logout?service=http://localhost:8080/oauth";

        // CAS客户端host
        private String casService = "http://localhost:8080/oauth";

        // CAS客户端拦截器拦截路径
        private String casServiceLogin = "/login/cas";

        // CAS客户端登出
        private String casServiceLogout = "/logout";
    }
}
