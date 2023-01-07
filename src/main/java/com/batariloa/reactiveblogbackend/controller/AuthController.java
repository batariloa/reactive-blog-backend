package com.batariloa.reactiveblogbackend.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("auth")
public class AuthController {


    @PostMapping("login")
    public Mono<String> login(){

        return Mono.just("login page");
    }
}
