package com.ecole.covoiturage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactiver CSRF pour simplifier (Vaadin gere sa propre protection)
            .csrf(csrf -> csrf.disable())
            // Configuration des autorisations - tout est public, gestion dans Vaadin
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Desactiver la page de login par defaut de Spring Security
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            // Permettre les frames pour Vaadin
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}
