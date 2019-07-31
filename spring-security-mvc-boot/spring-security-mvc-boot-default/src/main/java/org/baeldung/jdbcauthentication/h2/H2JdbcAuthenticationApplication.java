package org.baeldung.jdbcauthentication.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class H2JdbcAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(H2JdbcAuthenticationApplication.class, args);
    }

}
