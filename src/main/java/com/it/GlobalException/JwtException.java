package com.it.GlobalException;

public class JwtException extends RuntimeException{

    public JwtException(String message) {
        super(message);
    }
}
