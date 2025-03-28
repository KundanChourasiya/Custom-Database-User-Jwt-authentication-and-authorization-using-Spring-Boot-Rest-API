package com.it.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
//                .authorizeHttpRequests(auth->auth.anyRequest().permitAll())
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(
                                "/api/v1/user/signup",
                                "/api/v1/auth/login",
                                "/api/v1/admin/signup",
                                "/login/user/details"
                        )
                        .permitAll()
//                        .requestMatchers("api/v1/country").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
