package com.it.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "This Filed is required")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "This Filed is required")
    @Email(message = "Enter the Valid mail id")
    private String email;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "This Filed is required")
    private String name;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "This Filed is required")
    private String password;

}