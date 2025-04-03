package com.it.service.Impl;

import com.it.entity.AppUser;
import com.it.payload.ApiResponseDto;
import com.it.payload.AppUserDto;
import com.it.payload.LoginDto;
import com.it.payload.TokenDto;
import com.it.repository.AppUserRepository;
import com.it.service.AppUserService;
import com.it.service.Jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createUserSignup(AppUserDto dto) throws IllegalArgumentException {
        // Convert DTO to entity
        AppUser user = mapToAppUser(dto);

        // Check if email or username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "User email already exists."));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "Username already exists."));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
    }


    // create admin sign up
    @Override
    public ResponseEntity<?> createAdminSignup(AppUserDto dto) {
        // Convert DTO to entity
        AppUser user = mapToAppUser(dto);

        // Check if email or username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "User email already exists."));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), "Username already exists."));
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
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
    }

    // login user, verify user and generate token
    @Override
    public ResponseEntity<?> verifyCredential(LoginDto dto) {
        final Optional<AppUser> user = userRepository.findByUsername(dto.getUsername());
        if (user.isPresent()) {
            AppUser appUser = user.get();
            if (BCrypt.checkpw(dto.getPassword(), appUser.getPassword())) {

                // generate token
                String token = jwtService.generateToken(appUser.getUsername());
                TokenDto tokenDto = new TokenDto(token, "Jwt");

                return ResponseEntity.status(HttpStatus.OK.value()).body(tokenDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(new ApiResponseDto(HttpStatus.UNAUTHORIZED.value(), "Enter valid password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(new ApiResponseDto(HttpStatus.NOT_FOUND.value(), "Enter valid email"));
        }
    }

    // All users List
    @Override
    public List<AppUserDto> getAllUsers() {
        List<AppUser> appUsers = userRepository.findAll();
        List<AppUserDto> userDtoList = appUsers.stream().map(u -> mapToDto(u)).collect(Collectors.toList());
        return userDtoList;
    }
}
