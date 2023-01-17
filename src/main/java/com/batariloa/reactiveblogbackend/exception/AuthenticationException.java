package com.batariloa.reactiveblogbackend.exception;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(){
        super("Authentication failed.");
    }
}
