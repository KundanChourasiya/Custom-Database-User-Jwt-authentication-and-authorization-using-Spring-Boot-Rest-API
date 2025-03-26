package com.it.GlobalException;

import com.it.payload.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.lang.IllegalAccessException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerInvalidArgument(MethodArgumentNotValidException ex) {

        Map<String, String> errorMsg = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMsg.put(error.getField(), error.getDefaultMessage()));
        return errorMsg;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> ApplicationGlobalException(Exception e, WebRequest request){
        ErrorDto errorDto = new ErrorDto(e.getMessage(), LocalDate.now(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ErrorDto> IllegalAccessExceptionHandler(IllegalAccessException e, WebRequest request){
        ErrorDto errorDto = new ErrorDto(e.getMessage(), LocalDate.now(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
