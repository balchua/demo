package org.bal.frontend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /*
    For now we add the /api/ as unsecured.
     */
    private final String[] unsecuredUri = {"/webjars/**", "/actuator/**",
            "/healthz", "/assets/**", "/css/**", "/js/**", "/index.html", "/api/**", "/"};

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(unsecuredUri).permitAll()
                .anyRequest().authenticated().and()
                .oauth2ResourceServer().jwt();
    }

}
