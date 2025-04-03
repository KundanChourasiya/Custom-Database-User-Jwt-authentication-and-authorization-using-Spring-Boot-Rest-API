package com.it.controller;

import com.it.payload.AppUserDto;
import com.it.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Open endpoint url Controller", description = "To perform fetch all user/admin details without authentication and authorization")
public class OpenUrlController {

    @Autowired
    private AppUserService service;

    // url: http://localhost:8080/api/v1/all-user-list
    @Operation(
            summary = "Get operation for fetch the User/Admin both Details",
            description = "It is used to fetch the all user details with authentication and authorization this endpoint is open for all users."
    )
    @GetMapping("all-user-list")
    public ResponseEntity<List<AppUserDto>> getUserList(){
        List<AppUserDto> allUsers = service.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.FOUND);
    }
}
