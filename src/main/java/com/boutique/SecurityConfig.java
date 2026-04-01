package com.boutique;
import org.springframework.http.HttpMethod;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Specific rules go FIRST
            .requestMatchers(HttpMethod.GET, "/api/boutique/admin/products/*/image/**").permitAll()
            // General rules go LAST
            .anyRequest().permitAll()
        )
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable());

    return http.build();
}
}