package com.it.GlobalException;

import com.it.payload.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    // GlobalException Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> GlobalExceptionHandler(Exception ex, WebRequest request) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    //NoResourceFoundException Handler
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDto<Object>> NoResourceFoundExceptionHandler(NoResourceFoundException ex, WebRequest request) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // IllegalArgumentException Handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Object>> IllegalArgumentExceptionHandler(IllegalArgumentException ex, WebRequest request) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // InvalidCredentialsException Handler
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponseDto<Object>> UserNotFoundExceptionHandler(InvalidCredentialsException ex, WebRequest request) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // RuntimeException Handler
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto<Object>> RuntimeExceptionHandler(RuntimeException ex, WebRequest request) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // JwtException Handler
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseDto<Object>> JwtExceptionHandler(JwtException ex, WebRequest request) {
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // MethodArgumentNotValidExceptionHandler Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Object>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errorMsg = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMsg.put(error.getField(), error.getDefaultMessage()));
        ApiResponseDto<Object> response = new ApiResponseDto<>(false, "Something went wrong", errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
