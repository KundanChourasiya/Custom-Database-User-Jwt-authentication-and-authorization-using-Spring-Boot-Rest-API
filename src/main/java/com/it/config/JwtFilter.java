package com.it.config;

import com.it.GlobalException.JwtException;
import com.it.entity.AppUser;
import com.it.repository.AppUserRepository;
import com.it.service.Jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Handle Filter level exception
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    // list for permitted endpoints (like login/register/ open any endpoints)
    private static final List<String> PERMITTED_PATHS = List.of(
            "/api/v1/auth",
            "/api/v1/all-user-list",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars"
    );


    // Skip filter for permitted endpoints (like login/register/ open any endpoints)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PERMITTED_PATHS.stream().anyMatch(path ->
                request.getServletPath().startsWith(path)
        );
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtException("Missing or invalid Bearer Token Authorization header");
            }

            String token = authHeader.substring(7);
            String username = jwtService.verifyToken(token);

            Optional<AppUser> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new JwtException("User not found");
            }

            AppUser appUser = user.get();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    appUser,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(appUser.getRole()))
            );
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            exceptionResolver.resolveException(request, response, null, e);
        }
    }

}