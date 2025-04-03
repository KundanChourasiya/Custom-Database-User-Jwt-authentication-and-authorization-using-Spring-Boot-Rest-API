package com.it.GlobalException;

import com.it.payload.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    // GlobalException Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> globalExceptionHandler(Exception e, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //NoResourceFoundException Handler
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDto> NoResourceFoundExceptionHandler(NoResourceFoundException e, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    // IllegalArgumentException Handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> IllegalArgumentExceptionHandler(IllegalArgumentException e, WebRequest request){
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    // RuntimeException Handler
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> RuntimeExceptionHandler(RuntimeException e, WebRequest request){
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    // JwtException Handler
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDto> JwtExceptionHandler(JwtException e, WebRequest request){
        ErrorDto errorDto = new ErrorDto(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }

    // MethodArgumentNotValidExceptionHandler Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error->errorMap.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }


}
