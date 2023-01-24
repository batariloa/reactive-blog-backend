package com.batariloa.reactiveblogbackend.service;

import com.batariloa.reactiveblogbackend.controller.BlogPostController;
import com.batariloa.reactiveblogbackend.dto.*;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import com.batariloa.reactiveblogbackend.user.Role;
import com.batariloa.reactiveblogbackend.user.User;
import com.batariloa.reactiveblogbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);

    public Mono<MessageResponse> signUp(Mono<RegisterRequest> request) {

        return request.map(this::registerToUser)
                      .flatMap(user -> userRepository.existsByEmail(user.getEmail())
                                                     .flatMap(u -> {
                                                         if (u)
                                                             return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists."));
                                                         return userRepository.existsByUsername(user.getUsername());
                                                     })
                                                     .flatMap(u -> {
                                                         if (u)
                                                             return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken."));
                                                         return userRepository.save(user)
                                                                              .flatMap(savedUser -> Mono.just(new MessageResponse("User registered.")));
                                                     }));
    }

    public User registerToUser(RegisterRequest rq) {
        return User.builder()
                   .email(rq.getEmail())
                   .firstname(rq.getFirstname())
                   .lastname(rq.getLastname())
                   .username(rq.getUsername())
                   .password(passwordEncoder.encode(rq.getPassword()))
                   .refreshToken("")
                   .role(Role.USER)
                   .build();


    }


    public Mono<ResponseEntity<AuthResponse>> login(AuthRequest ar, ServerWebExchange exchange) {

        logger.warn("LOGIN CALLED");
        return userRepository.findByEmail(ar.getEmail())
                             .filter(user -> passwordEncoder.matches(ar.getPassword(), user.getPassword()))

                             .map(user -> {

                                 String refreshToken = jwtUtil.generateRefreshToken(user);
                                 user.setRefreshToken(refreshToken);


                                 exchange.getResponse()
                                         .addCookie(ResponseCookie.from("refreshToken", refreshToken)
                                                                  .httpOnly(true)
                                                                  .path("/")
                                                                  .secure(false)
                                                                  .build());

                                 exchange.getResponse()
                                         .addCookie(ResponseCookie.from("token", jwtUtil.generateToken(user))
                                                                  .httpOnly(true)
                                                                  .path("/")
                                                                  .maxAge(300)

                                                                  .secure(false)
                                                                  .build());


                                 return user;

                             })
                             .doOnNext(user -> {
                                         userRepository.save(user)
                                                       .subscribe();

                                     }


                             )
                             .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user), user.getUsername(), user.getId(), user.getRole())))
                             .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication error"))));


    }

    public Mono<ResponseEntity<MessageResponse>> refreshToken(ServerWebExchange exchange) {

        HttpCookie cookie = exchange.getRequest()
                                    .getCookies()
                                    .getFirst("refreshToken");


        //if refresh cookie is present
        if (cookie == null)
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication error"));

        logger.warn("REFRESH TOKEN IN SERVICE " + cookie.getValue());

        String refreshToken = cookie.getValue();

        return userRepository.findByRefreshToken(refreshToken)
                             .map(jwtUtil::generateToken)

                             .map(token -> {

                                 exchange.getResponse()
                                         .addCookie(ResponseCookie.from("token", token)
                                                                  .httpOnly(true)
                                                                  .path("/")
                                                                  .secure(false)
                                                                  .build());
                                 return ResponseEntity.ok(new MessageResponse("sdsa"));
                             })
                             .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication error")));

    }


    public Flux<SearchUserDto> searchUser(String query) {

        return userRepository.searchUsersByUsername(query);
    }
}
