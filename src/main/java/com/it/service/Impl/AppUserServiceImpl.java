package com.it.service.Impl;

import com.it.GlobalException.InvalidCredentialsException;
import com.it.entity.AppUser;
import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import com.it.repository.AppUserRepository;
import com.it.service.AppUserService;
import com.it.service.Jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public AppUserServiceImpl(AppUserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    // Map AppUser to dto
    private static AppUserDto mapToDto(AppUser user) {
        AppUserDto dto = new AppUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    // Map dto to AppUser
    private static AppUser mapToAppUser(AppUserDto dto) {
        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }


    // create user sign up
    @Override
    public AppUserDto createUserSignup(AppUserDto dto) throws IllegalArgumentException {
        // Convert DTO to entity
        AppUser user = mapToAppUser(dto);

        // Check if email or username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User email already exist.");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User Username already exist.");
        }

        // Set  decryptPassword and save the user
        String decryptPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(4));
        user.setPassword(decryptPassword);

        // Set default role and save the user
        user.setRole("ROLE_USER");
        AppUser savedUser = userRepository.save(user);

        // Convert saved user entity to DTO
        AppUserDto savedUserDto = mapToDto(savedUser);

        // Return saved user details in JSON format
        return savedUserDto;
    }


    //    // create admin sign up
    @Override
    public AppUserDto createAdminSignup(AppUserDto dto) {
        // Convert DTO to entity
        AppUser user = mapToAppUser(dto);

        // Check if email or username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User email already exist.");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User Username already exist.");
        }

        // Set  decryptPassword and save the user
        String decryptPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt(4));
        user.setPassword(decryptPassword);

        // Set default role and save the user
        user.setRole("ROLE_ADMIN");
        AppUser savedUser = userRepository.save(user);

        // Convert saved user entity to DTO
        AppUserDto savedUserDto = mapToDto(savedUser);

        // Return saved user details in JSON format
        return savedUserDto;
    }

    //    // login user, verify user and generate token
    @Override
    public String verifyCredential(LoginDto dto) {
        final Optional<AppUser> user = userRepository.findByUsername(dto.getUsername());
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or user not found");
        }
        AppUser appUser = user.get();
        if (!BCrypt.checkpw(dto.getPassword(), appUser.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        // generate token
        String token = jwtService.generateToken(appUser.getUsername());
        return token;
    }

    // All users List
    @Override
    public List<AppUserDto> getAllUsers() {
        List<AppUser> appUsers = userRepository.findAll();
        List<AppUserDto> userDtoList = appUsers.stream().map(u -> mapToDto(u)).collect(Collectors.toList());
        return userDtoList;
    }
}
