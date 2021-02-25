package com.pegasus.platform.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by enHui.Chen on 2020/9/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoPlatformPluginConfiguration.class)
public @interface EnablePlatformPlugin {
}
