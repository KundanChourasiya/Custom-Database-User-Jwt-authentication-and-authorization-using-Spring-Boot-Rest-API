package com.it.controller;

import com.it.entity.AppUser;
import com.it.repository.AppUserRepository;
import com.it.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository  appUserRepository;


    // url: http://localhost:8080/api/v1/user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<String> userEndPoint(@RequestHeader("Authorization") String authHeader) {
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            String tokenVal = authHeader.substring(7, authHeader.length());
            String username = jwtService.getUsername(tokenVal);
            Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
            if (byUsername.isPresent()){
                AppUser appUser = byUsername.get();
                return ResponseEntity.ok("Username: " + appUser.getName() +" UserRole: "+ appUser.getRole());
            }
        }
        return ResponseEntity.badRequest().body("Invalid token!");
    }

    // url: http://localhost:8080/api/v1/admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminEndPoint(@RequestHeader("Authorization") String authHeader) {
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            String tokenVal = authHeader.substring(7, authHeader.length());
            String username = jwtService.getUsername(tokenVal);
            Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
            if (byUsername.isPresent()){
                AppUser appUser = byUsername.get();
                return ResponseEntity.ok("Username: " + appUser.getName() +" UserRole: "+ appUser.getRole());
            }
        }
        return ResponseEntity.badRequest().body("Invalid token!");
    }
}
