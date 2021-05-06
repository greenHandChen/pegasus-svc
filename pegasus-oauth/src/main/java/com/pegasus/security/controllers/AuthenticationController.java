package com.pegasus.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by enHui.Chen on 2021/4/22.
 */
@RestController
@RequestMapping("/api/oauth")
public class AuthenticationController {
    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }
}
