package com.gh.pegasus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by enHui.Chen on 2020/9/15.
 */
@Slf4j
@SpringBootApplication
public class PegasusGatewayApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(PegasusGatewayApplication.class, args);
        } catch (Exception e) {
            log.error("start gateway error:", e);
        }
    }
}
