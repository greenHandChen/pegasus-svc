package com.gh.pegasus.support;

/**
 * Created by enHui.Chen on 2020/10/16.
 */
public interface GatewayHelperFilter {

    /**
     * @Author: enHui.Chen
     * @Description: 指定拦截优先级
     * @Data 2020/10/16
     */
    int filterOrder();


    /**
     * @Author: enHui.Chen
     * @Description: 拦截逻辑: 执行chain.doFilter()调用下一个拦截器,执行return后面拦截器不再调用
     * @Data 2020/10/16
     */
    void doFilter(GatewayContext gatewayContext, GatewayFilterHelperChain.InnerGatewayFilterHelperChain chain);
}
