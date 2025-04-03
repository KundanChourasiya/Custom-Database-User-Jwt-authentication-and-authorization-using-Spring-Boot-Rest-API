package com.it.payload;

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
public class LoginDto {

    @NotBlank(message = "Username field is required.")
    private String username;

    @NotBlank(message = "Password field is required.")
    @Size(min = 5, message = "Password must be up to 5 Character.")
    private String password;

}
