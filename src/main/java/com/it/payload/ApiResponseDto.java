package com.it.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto {

    private int status;
    private String message;

    public ApiResponseDto() {
    }

    public ApiResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
