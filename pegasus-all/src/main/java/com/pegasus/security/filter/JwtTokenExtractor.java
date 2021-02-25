package com.pegasus.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by enHui.Chen on 2020/10/23.
 */
@Slf4j
public class JwtTokenExtractor implements TokenExtractor {
    private static final String JWT_TOKEN_HEADER = "jwt_token";

    @Override
    public Authentication extract(HttpServletRequest request) {
        String tokenValue = extractToken(request);
        if (tokenValue != null) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
            return authentication;
        }
        return null;
    }

    protected String extractToken(HttpServletRequest request) {
        // first check the header...
        String value = null;

        // 从header取jwtToken
        Enumeration<String> headers = request.getHeaders(JWT_TOKEN_HEADER);
        // 通常意义上只有一个
        while (headers.hasMoreElements()) {
            value = headers.nextElement();
            if (value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
                break;
            }
        }

        // 从request取jwtToken
        if (value == null) {
            log.debug("Token not found in headers. Trying request parameters.");
            value = request.getParameter(JWT_TOKEN_HEADER);
        }

        if (value == null) {
            log.debug("Token not found in request parameters.  Not an OAuth2 request.");
            return null;
        }

        request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, OAuth2AccessToken.BEARER_TYPE);

        String token = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
        int commaIndex = token.indexOf(',');
        if (commaIndex > 0) {
            token = token.substring(0, commaIndex);
        }

        return token;
    }
}
