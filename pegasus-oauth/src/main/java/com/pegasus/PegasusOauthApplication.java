package com.pegasus;

import com.pegasus.platform.config.EnablePlatformPlugin;
import com.pegasus.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by enHui.Chen on 2020/9/15.
 */
@Slf4j
@EnableDiscoveryClient
@EnablePlatformPlugin
@SpringBootApplication
public class PegasusOauthApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(PegasusOauthApplication.class, args);
        } catch (Exception e) {
            log.error("start oauth error:", e);
        }
    }
}
