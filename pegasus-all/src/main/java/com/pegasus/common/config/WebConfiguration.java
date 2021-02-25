//package com.fullstack.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.ResourceUtils;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
///**
// * Created by enHui.Chen on 2019/8/28.
// */
//@Configuration
//public class WebConfiguration extends WebMvcConfigurationSupport {
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    protected void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
////        registry.addViewController("/admin/hello").setViewName("hello");
////        registry.addViewController("/").setViewName("hello");
//    }
//}
