package org.bal.frontend.controller;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Set;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @Autowired
    private KeycloakSecurityContext securityContext;

    @GetMapping("/admin")

    public String admin(Principal principal) {
        log.debug("Access Token: {} ", securityContext.getIdTokenString());

        AccessToken accessToken = securityContext.getToken();
        String username = accessToken.getPreferredUsername();
        String emailID = accessToken.getEmail();
        String lastname = accessToken.getFamilyName();
        String realmName = accessToken.getIssuer();
        AccessToken.Access realmAccess = accessToken.getRealmAccess();
        Set<String> roles = realmAccess.getRoles();

        for (String role: roles) {
            log.debug ("Available Role: {}", role);
        }
        log.debug("Username: {} " , username);
        return "index";
    }

}
