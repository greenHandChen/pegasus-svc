package com.gh.pegasus.autoconfigure;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by enHui.Chen on 2021/2/26.
 */
@Configuration
@ComponentScan({"com.gh.pegasus"})
@EnableConfigurationProperties(CasConfigurationProperties.class)
//@Import({DataSourceAutoConfiguration.class})
public class CasServerAutoConfiguration implements AuthenticationEventExecutionPlanConfigurer {
}
