package com.pegasus.platform.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by enHui.Chen on 2020/9/17.
 */
@Configuration
@ComponentScan(basePackages = "com.pegasus.platform")
@MapperScan({"com.pegasus.platform.mapper"})// mybatis
@EnableJpaRepositories(basePackages = "com.pegasus.platform")// TODO 待移除
public class AutoPlatformPluginConfiguration {
}
