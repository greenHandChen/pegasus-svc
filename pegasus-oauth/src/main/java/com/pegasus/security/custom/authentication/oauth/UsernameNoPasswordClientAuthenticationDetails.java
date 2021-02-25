package com.pegasus.security.custom.authentication.oauth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by enHui.Chen on 2020/9/14.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UsernameNoPasswordClientAuthenticationDetails extends WebAuthenticationDetails {

    private String username;
    private String clientId;
    private String secret;
    private String grantType;

    public UsernameNoPasswordClientAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.username = request.getParameter("username");
        this.clientId = request.getParameter("client_id");
        this.secret = request.getParameter("client_secret");
        this.grantType = request.getParameter("grant_type");
    }
}
