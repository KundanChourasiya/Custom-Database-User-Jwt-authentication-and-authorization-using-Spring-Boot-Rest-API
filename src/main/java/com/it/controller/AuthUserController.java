package com.it.controller;

import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import com.it.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Auth User Controller", description = "To perform user/admin signup, login and generate Jwt token")
public class AuthUserController {

    @Autowired
    private AppUserService appUserService;

    // Url: http://localhost:8080/api/v1/auth/user/signup
    @Operation(
            summary = "Post operation for signup user",
            description = "It is used to save user in database"
    )
    @PostMapping("/auth/user/signup")
    public ResponseEntity<?> createUser( @Valid @RequestBody AppUserDto dto) {
            return appUserService.createUserSignup(dto);

    }

    // Url: http://localhost:8080/api/v1/auth/admin/signup
    @Operation(
            summary = "Post operation for signup admin",
            description = "It is used to save admin in database"
    )
    @PostMapping("/auth/admin/signup")
    public ResponseEntity<?> createAdmin( @Valid @RequestBody AppUserDto dto) {
        return appUserService.createAdminSignup(dto);

    }

    // Url: http://localhost:8080/api/v1/auth/login
    @Operation(
            summary = "Post operation for login user/admin",
            description = "It is used to verify user/admin details and generate token"
    )
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        return appUserService.verifyCredential(dto);
    }

}
