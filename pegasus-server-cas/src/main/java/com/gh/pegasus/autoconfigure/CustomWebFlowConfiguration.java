package com.gh.pegasus.autoconfigure;

import com.gh.pegasus.security.CustomWebflowConfigurer;
import org.apereo.cas.config.CasWebflowContextConfiguration;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * Created by enHui.Chen on 2021/2/26.
 */
@Configuration
@AutoConfigureBefore(value = CasWebflowContextConfiguration.class)
public class CustomWebFlowConfiguration {
    @Autowired
    @Qualifier("logoutFlowRegistry")
    private FlowDefinitionRegistry logoutFlowRegitry;

    @Autowired
    @Qualifier("loginFlowRegistry")
    private FlowDefinitionRegistry loginFlowRegistry;

    @Autowired
    @Qualifier("builder")
    private FlowBuilderServices builder;

    /**
     * @param
     * @return CasWebflowConfigurer
     * @throws
     * @desc <TODO>
     * @date 2021-02-26 23:36:43
     */
    @Bean
    public CasWebflowConfigurer customWebflowConfigurer() {
        final CustomWebflowConfigurer customWebflowConfigurer = new CustomWebflowConfigurer(builder, loginFlowRegistry);
        customWebflowConfigurer.setLogoutFlowDefinitionRegistry(logoutFlowRegitry);
        return customWebflowConfigurer;
    }
}
