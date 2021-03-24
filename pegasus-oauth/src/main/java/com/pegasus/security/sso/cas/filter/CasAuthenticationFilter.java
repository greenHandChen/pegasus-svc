package com.pegasus.security.sso.cas.filter;/*
 * Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.pegasus.security.sso.cas.provider.CasUsernamePasswordAuthenticationToken;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author enhui.chen
 * @desc 自定义CASFilter
 * @date 2021-03-01 18:43:52
 */
public class CasAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    /**
     * Used to identify a CAS request for a stateful user agent, such as a web browser.
     */
    public static final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";

    /**
     * Used to identify a CAS request for a stateless user agent, such as a remoting
     * protocol client (e.g. Hessian, Burlap, SOAP etc). Results in a more aggressive
     * caching strategy being used, as the absence of a <code>HttpSession</code> will
     * result in a new authentication attempt on every request.
     */
    public static final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

    /**
     * The last portion of the receptor url, i.e. /proxy/receptor
     */
    private RequestMatcher proxyReceptorMatcher;

    /**
     * The backing storage to store ProxyGrantingTicket requests.
     */
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;

    private String artifactParameter = ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER;

    private boolean authenticateAllArtifacts;

    private AuthenticationFailureHandler proxyFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    // ~ Constructors
    // ===================================================================================================

    public CasAuthenticationFilter() {
        // ant-style
        super("/login/cas");
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    protected final void successfulAuthentication(HttpServletRequest request,
                                                  HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        boolean continueFilterChain = proxyTicketRequest(
                serviceTicketRequest(request, response), request);
        if (!continueFilterChain) {
            super.successfulAuthentication(request, response, chain, authResult);
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                    + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }

        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException,
            IOException {
        // if the request is a proxy request process it and return null to indicate the
        // request has been processed
        if (proxyReceptorRequest(request)) {
            logger.debug("Responding to proxy receptor request");
            CommonUtils.readAndRespondToProxyReceptorRequest(request, response,
                    this.proxyGrantingTicketStorage);
            return null;
        }

        final boolean serviceTicketRequest = serviceTicketRequest(request, response);
        final String username = serviceTicketRequest ? CAS_STATEFUL_IDENTIFIER : CAS_STATELESS_IDENTIFIER;
        String password = obtainArtifact(request);

        if (password == null) {
            logger.debug("Failed to obtain an artifact (cas ticket)");
            password = "";
        }

        // 自定义token
        final CasUsernamePasswordAuthenticationToken authRequest = new CasUsernamePasswordAuthenticationToken(username, password);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * If present, gets the artifact (CAS ticket) from the {@link HttpServletRequest}.
     *
     * @param request
     * @return if present the artifact from the {@link HttpServletRequest}, else null
     */
    protected String obtainArtifact(HttpServletRequest request) {
        return request.getParameter(artifactParameter);
    }

    /**
     * Overridden to provide proxying capabilities.
     */
    protected boolean requiresAuthentication(final HttpServletRequest request,
                                             final HttpServletResponse response) {
        final boolean serviceTicketRequest = serviceTicketRequest(request, response);
        final boolean result = serviceTicketRequest || proxyReceptorRequest(request)
                || (proxyTicketRequest(serviceTicketRequest, request));
        if (logger.isDebugEnabled()) {
            logger.debug("requiresAuthentication = " + result);
        }
        return result;
    }

    /**
     * Sets the {@link AuthenticationFailureHandler} for proxy requests.
     *
     * @param proxyFailureHandler
     */
    public final void setProxyAuthenticationFailureHandler(
            AuthenticationFailureHandler proxyFailureHandler) {
        Assert.notNull(proxyFailureHandler, "proxyFailureHandler cannot be null");
        this.proxyFailureHandler = proxyFailureHandler;
    }

    /**
     * Wraps the {@link AuthenticationFailureHandler} to distinguish between handling
     * proxy ticket authentication failures and service ticket failures.
     */
    @Override
    public final void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(new CasAuthenticationFailureHandler(
                failureHandler));
    }

    public final void setProxyReceptorUrl(final String proxyReceptorUrl) {
        this.proxyReceptorMatcher = new AntPathRequestMatcher("/**" + proxyReceptorUrl);
    }

    public final void setProxyGrantingTicketStorage(
            final ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
        this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
    }

    public final void setServiceProperties(final ServiceProperties serviceProperties) {
        this.artifactParameter = serviceProperties.getArtifactParameter();
        this.authenticateAllArtifacts = serviceProperties.isAuthenticateAllArtifacts();
    }

    /**
     * Indicates if the request is elgible to process a service ticket. This method exists
     * for readability.
     *
     * @param request
     * @param response
     * @return
     */
    private boolean serviceTicketRequest(final HttpServletRequest request,
                                         final HttpServletResponse response) {
        boolean result = super.requiresAuthentication(request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("serviceTicketRequest = " + result);
        }
        return result;
    }

    /**
     * Indicates if the request is elgible to process a proxy ticket.
     *
     * @param request
     * @return
     */
    private boolean proxyTicketRequest(final boolean serviceTicketRequest,
                                       final HttpServletRequest request) {
        if (serviceTicketRequest) {
            return false;
        }
        final boolean result = authenticateAllArtifacts
                && obtainArtifact(request) != null && !authenticated();
        if (logger.isDebugEnabled()) {
            logger.debug("proxyTicketRequest = " + result);
        }
        return result;
    }

    /**
     * Determines if a user is already authenticated.
     *
     * @return
     */
    private boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * Indicates if the request is elgible to be processed as the proxy receptor.
     *
     * @param request
     * @return
     */
    private boolean proxyReceptorRequest(final HttpServletRequest request) {
        final boolean result = proxyReceptorConfigured()
                && proxyReceptorMatcher.matches(request);
        if (logger.isDebugEnabled()) {
            logger.debug("proxyReceptorRequest = " + result);
        }
        return result;
    }

    /**
     * Determines if the {@link CasAuthenticationFilter} is configured to handle the proxy
     * receptor requests.
     *
     * @return
     */
    private boolean proxyReceptorConfigured() {
        final boolean result = this.proxyGrantingTicketStorage != null
                && proxyReceptorMatcher != null;
        if (logger.isDebugEnabled()) {
            logger.debug("proxyReceptorConfigured = " + result);
        }
        return result;
    }

    /**
     * A wrapper for the AuthenticationFailureHandler that will flex the
     * {@link AuthenticationFailureHandler} that is used. The value
     * {@link CasAuthenticationFilter#setProxyAuthenticationFailureHandler(AuthenticationFailureHandler)
     * will be used for proxy requests that fail. The value
     * {@link CasAuthenticationFilter#setAuthenticationFailureHandler(AuthenticationFailureHandler)}
     * will be used for service tickets that fail.
     *
     * @author Rob Winch
     */
    private class CasAuthenticationFailureHandler implements AuthenticationFailureHandler {
        private final AuthenticationFailureHandler serviceTicketFailureHandler;

        public CasAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
            Assert.notNull(failureHandler, "failureHandler");
            this.serviceTicketFailureHandler = failureHandler;
        }

        public void onAuthenticationFailure(HttpServletRequest request,
                                            HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            if (serviceTicketRequest(request, response)) {
                serviceTicketFailureHandler.onAuthenticationFailure(request, response,
                        exception);
            } else {
                proxyFailureHandler.onAuthenticationFailure(request, response, exception);
            }
        }
    }
}