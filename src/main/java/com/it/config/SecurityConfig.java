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
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
//                .authorizeHttpRequests(auth-> auth.anyRequest().permitAll())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/v2/api-docs",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/webjars/**",    // Required for Swagger UI assets
                                "/api/v1/all-user-list"
                        )
                        .permitAll()
//                        .requestMatchers("/hms/api/v1/greet").hasAuthority("USER")   // hasAuthority() instead of hasRole() spring 3
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
