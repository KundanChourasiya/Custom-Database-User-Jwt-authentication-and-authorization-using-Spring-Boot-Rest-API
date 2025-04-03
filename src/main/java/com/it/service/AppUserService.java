package com.it.service;

import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppUserService {

    // create user sign up
    ResponseEntity<?> createUserSignup(AppUserDto dto);

    // create admin sign up
    ResponseEntity<?> createAdminSignup(AppUserDto dto);

    // login user and verify user
    ResponseEntity<?> verifyCredential(LoginDto dto);

    // All users List
    List<AppUserDto> getAllUsers();

}
