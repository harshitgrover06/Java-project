package com.example.datastructure.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String error) {
        super(error);
    }
}
