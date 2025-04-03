package com.it.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;

    @NotBlank(message = "Name field is required.")
    private String name;

    @NotBlank(message = "Email field is required.")
    @Email(message = "Enter a valid email.")
    private String email;

    @NotBlank(message = "Username field is required.")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)      // This will be hidden
    @NotBlank(message = "Password field is required.")
    @Size(min = 5, message = "Password must be up to 5 Character.")
    private String password;

    private String role;
}
