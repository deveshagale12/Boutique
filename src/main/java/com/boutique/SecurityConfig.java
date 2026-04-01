package com.boutique;


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
            // 1. Disable CSRF for REST APIs (otherwise POST requests fail)
            .csrf(csrf -> csrf.disable())
            
            // 2. Allow all requests to pass through Spring Security
            // Your ApiKeyInterceptor will handle the actual validation
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            
            // 3. Disable the default Login Form
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}