package com.gh.pegasus.support;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by enHui.Chen on 2020/10/16.
 */
@Slf4j
public class GatewayFilterHelperChain implements WebFilter {
    private List<GatewayHelperFilter> gatewayFilters;

    private static final ThreadLocal<GatewayContext> gatewayContextThreadLocal = new ThreadLocal<>();

    public GatewayFilterHelperChain() {

    }

    public GatewayFilterHelperChain(List<GatewayHelperFilter> gatewayFilters) {
        // 默认升序,返回值越小,优先级越高
        this.gatewayFilters = Optional.of(gatewayFilters).orElseGet(Collections::emptyList)
                .stream().sorted(Comparator.comparing(GatewayHelperFilter::filterOrder)).collect(Collectors.toList());
    }


    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        // 初始化上下文
        GatewayContext gatewayContext = initGatewayContext(serverWebExchange);
        ServerWebExchange webEx = null;
        try {
            InnerGatewayFilterHelperChain innerGatewayFilterChain = new InnerGatewayFilterHelperChain(this.gatewayFilters);
            innerGatewayFilterChain.doFilter(gatewayContext, innerGatewayFilterChain);

            // 设置额外请求头
            webEx = serverWebExchange
                    .mutate()
                    .request(builder -> gatewayContext.getAdditionalHeaders().forEach(builder::header))
                    .build();
        } catch (FeignException fException) {
            ServerHttpResponse serverHttpResponse = gatewayContext.getServerHttpResponse();
            StringBuilder responseMessage = new StringBuilder();

            int status = fException.status();
            // 未授权抛出异常
            if (status == HttpStatus.UNAUTHORIZED.value()) {
                HttpStatus requestStatus = HttpStatus.UNAUTHORIZED;
                // head
                HttpHeaders headers = serverHttpResponse.getHeaders();
                headers.put(HttpHeaders.ACCEPT_CHARSET, Collections.singletonList("utf-8"));
                headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("text/html;charset=UTF-8"));
                // status
                serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                // body
                responseMessage
                        .append("<oauth>")
                        .append("<status>").append(requestStatus.toString()).append("</status>")
                        .append("<code>").append(requestStatus.value()).append("</code>")
                        .append("<message>").append(requestStatus.toString()).append("</message>")
                        .append("</oauth>");
            }
            DataBufferFactory factory = serverHttpResponse.bufferFactory();
            DataBuffer dataBuffer = factory.wrap(responseMessage.toString().getBytes(StandardCharsets.UTF_8));
            return serverHttpResponse.writeAndFlushWith(Flux.just(dataBuffer).map(Flux::just));

        } finally {
            clearGatewayContext();

        }

        return webFilterChain.filter(webEx);
    }

    public static class InnerGatewayFilterHelperChain {
        private int currentPosition = -1;
        private int size;
        private List<GatewayHelperFilter> gatewayFilters;

        public InnerGatewayFilterHelperChain(List<GatewayHelperFilter> gatewayFilters) {
            this.gatewayFilters = gatewayFilters;
            this.size = gatewayFilters.size();
        }

        /**
         * @Author: enHui.Chen
         * @Description: 内部拦截器开始拦截
         * @Data 2020/10/19
         */
        public void doFilter(GatewayContext gatewayContext, InnerGatewayFilterHelperChain chain) {
            // 拦截结束
            if (currentPosition == this.size - 1) {
                if (log.isDebugEnabled()) {
                    log.debug("innerGatewayFilterChain is over");
                }
                return;
            }

            GatewayHelperFilter nextFilter = gatewayFilters.get(++currentPosition);

            if (log.isDebugEnabled()) {
                log.debug("current gatewayFilter is {}, currentPosition:{}", nextFilter.getClass().getSimpleName(), this.currentPosition);
            }

            nextFilter.doFilter(gatewayContext, chain);
        }
    }

    public List<GatewayHelperFilter> getGatewayFilters() {
        return gatewayFilters;
    }

    public void setGatewayFilters(List<GatewayHelperFilter> gatewayFilters) {
        this.gatewayFilters = gatewayFilters;
    }

    /**
     * @Author: enHui.Chen
     * @Description: 初始化上下文
     * @Data 2020/10/19
     */
    public static GatewayContext initGatewayContext(ServerWebExchange serverWebExchange) {
        GatewayContext gatewayContext = new GatewayContext();
        gatewayContext.setServerHttpRequest(serverWebExchange.getRequest());
        gatewayContext.setServerHttpResponse(serverWebExchange.getResponse());
        setGatewayContext(gatewayContext);
        return gatewayContext;
    }

    private static void setGatewayContext(GatewayContext gatewayContext) {
        gatewayContextThreadLocal.set(gatewayContext);
    }

    private static void clearGatewayContext() {
        gatewayContextThreadLocal.remove();
    }

}
