package com.it.service.Impl;

import com.it.entity.AppUser;
import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import com.it.payload.TokenDto;
import com.it.repository.AppUserRepository;
import com.it.service.AppUserService;
import com.it.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JwtService jwtService;


    // map AppUser To Dto
    private AppUserDto mapToAppUserDto(AppUser user){
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setRole(user.getRole());
        return dto;
    }

    // map AppUserDto To Entity
    private AppUser mapToAppUserEntity(AppUserDto dto){
        AppUser appUser = new AppUser();
        appUser.setId(dto.getId());
        appUser.setUsername(dto.getUsername());
        appUser.setEmail(dto.getEmail());
        appUser.setEmail(dto.getEmail());
        appUser.setRole(dto.getRole());
        return appUser;
    }

    @Override
    public AppUserDto userSignUp(AppUser user) throws IllegalAccessException {
        if(appUserRepository.existsByUsername(user.getUsername())){
            throw new IllegalAccessException("Username Already Exits");
        }
        if(appUserRepository.existsByEmail(user.getEmail())){
            throw new IllegalAccessException("Email Already Exits");
        }
        user.setRole("ROLE_USER");
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4));
        user.setPassword(encryptedPassword);
        AppUser appUser = appUserRepository.save(user);
        AppUserDto dto = mapToAppUserDto(appUser);
        return dto;
    }

    @Override
    public AppUserDto adminSignUp(AppUser user) throws IllegalAccessException {
        if(appUserRepository.existsByUsername(user.getUsername())){
            throw new IllegalAccessException("Username Already Exits");
        }
        if(appUserRepository.existsByEmail(user.getEmail())){
            throw new IllegalAccessException("Email Already Exits");
        }
        user.setRole("ROLE_ADMIN");
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4));
        user.setPassword(encryptedPassword);
        AppUser appUser = appUserRepository.save(user);
        AppUserDto dto = mapToAppUserDto(appUser);
        return dto;
    }

    @Override
    public String verifyUser(LoginDto dto) {
        Optional<AppUser> byUsername = appUserRepository.findByUsername(dto.getUsername());
        if (byUsername.isPresent()){
            AppUser appUser = byUsername.get();
            if (BCrypt.checkpw(dto.getPassword(), appUser.getPassword())){
                // generate Token
                String token = jwtService.generateToken(appUser.getUsername());
                return token;
            }
        }
        return null;
    }
}
