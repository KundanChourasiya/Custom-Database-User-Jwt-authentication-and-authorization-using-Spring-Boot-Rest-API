package com.it.controller;

import com.it.entity.AppUser;
import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import com.it.payload.TokenDto;
import com.it.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    // Url: http://localhost:8080/api/v1/user/signup
    @PostMapping("/user/signup")
    public ResponseEntity<?> createUser( @Valid @RequestBody AppUser user) throws IllegalAccessException {
            AppUserDto dto = appUserService.userSignUp(user);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // Url: http://localhost:8080/api/v1/admin/signup
    @PostMapping("/admin/signup")
    public ResponseEntity<?> createAdmin( @Valid @RequestBody AppUser user) throws IllegalAccessException {
        AppUserDto dto = appUserService.adminSignUp(user);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){

        String token = appUserService.verifyUser(dto);
        if (token!=null){
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(token);
            tokenDto.setType("JWT");
            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username/password", HttpStatus.FORBIDDEN);
    }

}
