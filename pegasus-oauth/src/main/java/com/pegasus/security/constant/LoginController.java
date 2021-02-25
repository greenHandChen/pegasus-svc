package com.pegasus.security.constant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by enHui.Chen on 2019/9/5.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String forwardLogin() {
        return "login";
    }
}
