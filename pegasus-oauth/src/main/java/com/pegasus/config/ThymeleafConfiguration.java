package com.pegasus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Created by enHui.Chen on 2018/3/21 0021.
 */
@Configuration
public class ThymeleafConfiguration {
    /**
     * @Author: enHui.Chen
     * @Description: 配置一个视图解析器
     * @Data 2018/3/23 0023
     */
    @Bean
    public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
        ClassLoaderTemplateResolver classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        classLoaderTemplateResolver.setPrefix("templates/");
        classLoaderTemplateResolver.setSuffix(".html");
        classLoaderTemplateResolver.setTemplateMode("HTML");
        classLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        classLoaderTemplateResolver.setOrder(1);
        classLoaderTemplateResolver.setCacheable(false);
        return classLoaderTemplateResolver;
    }
}
