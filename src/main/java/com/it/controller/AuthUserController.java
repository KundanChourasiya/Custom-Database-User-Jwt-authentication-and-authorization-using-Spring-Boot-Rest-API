package com.it.controller;

import com.it.payload.ApiResponseDto;
import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import com.it.payload.TokenDto;
import com.it.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    public AuthUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    // Url: http://localhost:8080/api/v1/auth/user/signup
    @Operation(
            summary = "Post operation for signup user",
            description = "It is used to save user in database"
    )
    @PostMapping("/auth/user/signup")
    public ResponseEntity<ApiResponseDto<?>>  createUser(@Valid @RequestBody AppUserDto dto) {
        AppUserDto userSignup = appUserService.createUserSignup(dto);
        ApiResponseDto<AppUserDto> userRegisterSuccessfully = new ApiResponseDto<>(true, "User Register successfully", userSignup);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.CREATED.value())).body(userRegisterSuccessfully);
    }

    // Url: http://localhost:8080/api/v1/auth/admin/signup
    @Operation(
            summary = "Post operation for signup admin",
            description = "It is used to save admin in database"
    )
    @PostMapping("/auth/admin/signup")
    public ResponseEntity<ApiResponseDto<?>>  createAdmin(@Valid @RequestBody AppUserDto dto) {
        AppUserDto adminSignup = appUserService.createAdminSignup(dto);
        ApiResponseDto<AppUserDto> adminRegisterSuccessfully = new ApiResponseDto<>(true, "Admin Register successfully", adminSignup);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.CREATED.value())).body(adminRegisterSuccessfully);
    }

    // Url: http://localhost:8080/api/v1/auth/login
    @Operation(
            summary = "Post operation for login user/admin",
            description = "It is used to verify user/admin details and generate token"
    )
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto<?>> login(@RequestBody LoginDto dto){
        String token = appUserService.verifyCredential(dto);
        TokenDto tokenDto = new TokenDto(token, "Jwt");
        ApiResponseDto<TokenDto> tokenCreated = new ApiResponseDto<>(true, "Token Generated", tokenDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.OK.value())).body(tokenCreated);
    }

}
