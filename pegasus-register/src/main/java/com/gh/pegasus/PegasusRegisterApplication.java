package com.gh.pegasus;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by enHui.Chen on 2020/9/15.
 */
@Slf4j
@EnableEurekaServer
@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class}
)
public class PegasusRegisterApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(PegasusRegisterApplication.class, args);
        } catch (Exception e) {
            log.error("start register error:", e);
        }
    }
}
