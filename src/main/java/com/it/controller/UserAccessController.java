package com.it.controller;

import com.it.entity.AppUser;
import com.it.payload.AppUserDto;
import com.it.repository.AppUserRepository;
import com.it.service.AppUserService;
import com.it.service.Jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
@Tag(name = "User/admin Access endpoint url Controller", description = "To perform user/admin details after jwt authentication and authorization")
public class UserAccessController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService service;


    // url: http://localhost:8080/api/v1/user
    @Operation(
            summary = "Get operation for fetch the user Details",
            description = "It is used to fetch the details if user token is valid and user has authorized for this endpoint."
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<String> userEndPoint(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String tokenVal = authHeader.substring(7);
            String username = jwtService.verifyToken(tokenVal);
            Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
            if (byUsername.isPresent()) {
                AppUser appUser = byUsername.get();
                return ResponseEntity.ok("Username: " + appUser.getName() + " UserRole: " + appUser.getRole());
            }
        }
        return ResponseEntity.badRequest().body("Invalid token!");
    }

    // url: http://localhost:8080/api/v1/admin
    @Operation(
            summary = "Get operation for fetch the Admin Details",
            description = "It is used to fetch the details if Admin token is valid and Admin has authorized for this endpoint."
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminEndPoint(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String tokenVal = authHeader.substring(7);
            String username = jwtService.verifyToken(tokenVal);
            Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
            if (byUsername.isPresent()) {
                AppUser appUser = byUsername.get();
                return ResponseEntity.ok("Username: " + appUser.getName() + " UserRole: " + appUser.getRole());
            }
        }
        return ResponseEntity.badRequest().body("Invalid token!");
    }

    // url: http://localhost:8080/api/v1/greet
    @Operation(
            summary = "Get operation for fetch the User/Admin both Details",
            description = "It is used to fetch the details if User/Admin both token is valid and User/Admin has authorized for this endpoint."
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/greet")
    public ResponseEntity<String> greetEndPoint(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String tokenVal = authHeader.substring(7);
            String username = jwtService.verifyToken(tokenVal);
            Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
            if (byUsername.isPresent()) {
                AppUser appUser = byUsername.get();
                return ResponseEntity.ok("Welcome, Username: " + appUser.getName() + " UserRole: " + appUser.getRole());
            }
        }
        return ResponseEntity.badRequest().body("Invalid token!");
    }

    // url: http://localhost:8080/api/v1/login/user/details
    @Operation(
            summary = "Get operation for fetch the User/Admin both Details",
            description = "It is used to fetch the details if User/Admin both token is valid and User/Admin has authorized for this endpoint."
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/login/user/details")
    public ResponseEntity<?> LoginUserDetails(@AuthenticationPrincipal AppUser user) {
        if (user!=null){
            AppUserDto dto = new AppUserDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }else {
            return ResponseEntity.badRequest().body("Invalid token!");
        }
    }
}
