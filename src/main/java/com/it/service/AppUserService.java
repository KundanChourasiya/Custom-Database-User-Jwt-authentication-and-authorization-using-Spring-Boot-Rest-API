package com.it.service;

import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;

import java.util.List;

public interface AppUserService {

    // create user sign up
    AppUserDto createUserSignup(AppUserDto dto);

    // create admin sign up
    AppUserDto createAdminSignup(AppUserDto dto);

    // login user and verify user
    String verifyCredential(LoginDto dto);

    // All users List
    List<AppUserDto> getAllUsers();

}
