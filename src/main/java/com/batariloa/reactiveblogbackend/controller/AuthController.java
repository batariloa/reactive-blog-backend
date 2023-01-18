package com.batariloa.reactiveblogbackend.controller;


import com.batariloa.reactiveblogbackend.dto.*;
import com.batariloa.reactiveblogbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {


    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {

        return authenticationService.login(ar);
    }

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MessageResponse> register(@RequestBody Mono<RegisterRequest> registerRequestMono) {

        return authenticationService.signUp(registerRequestMono);
    }

    @PostMapping("/search/{query}")
    public Flux<SearchUserDto> searchUser(@PathVariable String query) {

        return authenticationService.searchUser(query);
    }
}
