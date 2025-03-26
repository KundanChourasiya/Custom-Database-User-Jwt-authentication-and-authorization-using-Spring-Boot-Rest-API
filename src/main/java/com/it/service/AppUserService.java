package com.it.service;

import com.it.entity.AppUser;
import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;

public interface AppUserService {

    public AppUserDto userSignUp(AppUser user) throws IllegalAccessException;

    public AppUserDto adminSignUp(AppUser user) throws IllegalAccessException;

    String verifyUser(LoginDto dto);
}
