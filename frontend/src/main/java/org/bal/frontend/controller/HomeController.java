package org.bal.frontend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/admin")
    public String admin(Principal principal) {
        log.debug("Principal: {} ", principal.getName());

        return "index";
    }

}
