package com.gh.pegasus.support;

import com.gh.pegasus.support.dto.CuxUserDetails;
import com.gh.pegasus.support.utils.ServerRequestUtil;
import com.pegasus.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by enHui.Chen on 2020/10/16.
 */
public class GatewayContext {
    private ServerHttpRequest serverHttpRequest;

    private ServerHttpResponse serverHttpResponse;

    private CuxUserDetails cuxUserDetails;

    private Map<String, String> additionalHeaders = new HashMap<>();

    private static final String ACCESS_TOKEN_HEADER = "Authorization";
    private static final String ACCESS_TOKEN_TYPE = "Bearer ";

    public ServerHttpRequest getServerHttpRequest() {
        return serverHttpRequest;
    }

    public void setServerHttpRequest(ServerHttpRequest serverHttpRequest) {
        this.serverHttpRequest = serverHttpRequest;
    }

    public ServerHttpResponse getServerHttpResponse() {
        return serverHttpResponse;
    }

    public void setServerHttpResponse(ServerHttpResponse serverHttpResponse) {
        this.serverHttpResponse = serverHttpResponse;
    }

    public CuxUserDetails getCuxUserDetails() {
        return cuxUserDetails;
    }

    public void setCuxUserDetails(CuxUserDetails cuxUserDetails) {
        this.cuxUserDetails = cuxUserDetails;
    }

    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    public void setAdditionalHeaders(Map<String, String> additionalHeaders) {
        this.additionalHeaders = additionalHeaders;
    }

    public String getRequestPath() {
        return serverHttpRequest.getURI().getPath();
    }

    public String getAccessToken() {
        String accessToken = ServerRequestUtil.getHeaderByName(serverHttpRequest, ACCESS_TOKEN_HEADER);
        if (StringUtils.startsWithIgnoreCase(accessToken, ACCESS_TOKEN_TYPE)) {
            return accessToken.substring(ACCESS_TOKEN_TYPE.length());
        }
        return accessToken;
    }

    public String getJwtToken(Signer jwtSigner) {
        String cuxUserDetails = JsonUtil.toString(getCuxUserDetails());
        return ACCESS_TOKEN_TYPE + JwtHelper.encode(cuxUserDetails, jwtSigner).getEncoded();
    }

    public void addHeader(String headerName, String headerValue) {
        this.additionalHeaders.put(headerName, headerValue);
    }


}
