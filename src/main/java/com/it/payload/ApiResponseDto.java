package com.it.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto<T> {

    private Boolean status;
    private String message;
    private T data;

    public ApiResponseDto(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponseDto() {
    }
}
