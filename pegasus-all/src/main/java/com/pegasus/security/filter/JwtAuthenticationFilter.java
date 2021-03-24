package com.pegasus.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by enHui.Chen on 2020/10/23.
 */
@Slf4j
public class JwtAuthenticationFilter implements Filter {
    private TokenExtractor tokenExtractor;
    private ResourceServerTokenServices tokenService;
    private String resourceId;

    public JwtAuthenticationFilter(TokenExtractor tokenExtractor, ResourceServerTokenServices tokenService, String resourceId) {
        this.tokenExtractor = tokenExtractor;
        this.tokenService = tokenService;
        this.resourceId = resourceId;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 手动将bean注入容器
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final boolean debug = log.isDebugEnabled();
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {

            // 1.解析jwtToken为Authentication
            Authentication authentication = tokenExtractor.extract(request);

            if (authentication == null) {
                if (isAuthenticated()) {
                    if (debug) {
                        log.debug("Clearing security context.");
                    }
                    SecurityContextHolder.clearContext();
                }
                if (debug) {
                    log.debug("No jwt-token in request");
                }
                ((HttpServletResponse) servletResponse).sendError(HttpStatus.UNAUTHORIZED.value(), "no No jwt-token in request");
                return;
            } else {
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
                if (authentication instanceof AbstractAuthenticationToken) {
                    AbstractAuthenticationToken needsDetails = (AbstractAuthenticationToken) authentication;
                    needsDetails.setDetails(new OAuth2AuthenticationDetails(request));
                }

                // 2.进行认证鉴权
                Authentication authResult = this.authenticate(authentication);

                if (debug) {
                    log.debug("Authentication success: " + authResult);
                }

                SecurityContextHolder.getContext().setAuthentication(authResult);
                // 设置进缓存
                HttpSession session = request.getSession(false);
                session = session == null ? request.getSession() : session;
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            }
        } catch (OAuth2Exception failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                log.debug("Authentication request failed: " + failed);
            }

            return;
        }

        filterChain.doFilter(request, response);
    }


    @Override
    public void destroy() {

    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication == null) {
            throw new InvalidTokenException("Invalid token (token not found)");
        }
        String token = (String) authentication.getPrincipal();
        // 3.认证鉴权核心逻辑
        OAuth2Authentication auth = tokenService.loadAuthentication(token);
        if (auth == null) {
            throw new InvalidTokenException("Invalid token: " + token);
        }

        // 4.校验是否拥有访问该资源的权限
        Collection<String> resourceIds = auth.getOAuth2Request().getResourceIds();
        if (resourceId != null && resourceIds != null && !resourceIds.isEmpty() && !resourceIds.contains(resourceId)) {
            throw new OAuth2AccessDeniedException("Invalid token does not contain resource id (" + resourceId + ")");
        }

        if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            // Guard against a cached copy of the same details
            if (!details.equals(auth.getDetails())) {
                // Preserve the authentication details from the one loaded by token services
                details.setDecodedDetails(auth.getDetails());
            }
        }
        auth.setDetails(authentication.getDetails());
        auth.setAuthenticated(true);
        return auth;

    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

}
