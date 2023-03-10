package com.batariloa.reactiveblogbackend.controller;


import com.batariloa.reactiveblogbackend.dto.*;
import com.batariloa.reactiveblogbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar, ServerWebExchange exchange) {

        return authenticationService.login(ar, exchange);
    }

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MessageResponse> register(@RequestBody Mono<RegisterRequest> registerRequestMono) {

        return authenticationService.signUp(registerRequestMono);
    }

    @PostMapping(path = "refresh-token")
    public Mono<ResponseEntity<MessageResponse>> refreshToken(ServerWebExchange exchange) {


        logger.warn("CALLED REFRESH TOKEN.");
        return authenticationService.refreshToken(exchange);

    }

    @PostMapping("/search/{query}")

    public Flux<SearchUserDto> searchUser(@PathVariable String query) {

        return authenticationService.searchUser(query);
    }
}
