package com.batariloa.reactiveblogbackend.service;

import com.batariloa.reactiveblogbackend.dto.*;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import com.batariloa.reactiveblogbackend.user.Role;
import com.batariloa.reactiveblogbackend.user.User;
import com.batariloa.reactiveblogbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
                   .role(Role.USER)
                   .build();


    }


    public Mono<ResponseEntity<AuthResponse>> login(AuthRequest ar) {

        return userRepository.findByEmail(ar.getEmail())
                             .filter(user -> passwordEncoder.matches(ar.getPassword(), user.getPassword()))

                             .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user), user.getUsername(), user.getId(), user.getRole())))
                             .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication error"))));


    }

    public Flux<SearchUserDto> searchUser(String query) {

        return userRepository.searchUsersByUsername(query);
    }
}
