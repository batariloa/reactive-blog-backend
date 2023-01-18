package com.batariloa.reactiveblogbackend.service;

import com.batariloa.reactiveblogbackend.controller.AuthController;
import com.batariloa.reactiveblogbackend.dto.AuthRequest;
import com.batariloa.reactiveblogbackend.dto.AuthResponse;
import com.batariloa.reactiveblogbackend.dto.RegisterRequest;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import com.batariloa.reactiveblogbackend.user.Role;
import com.batariloa.reactiveblogbackend.user.User;
import com.batariloa.reactiveblogbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public Mono signUp(Mono<RegisterRequest> request) {


        return request.map(this::registerToUser)
                      .flatMap(user -> userRepository.findByEmail(user.getEmail())
                                                     .flatMap(user1 -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "error")))
                                                     .switchIfEmpty(userRepository.save(user)));
    }

    public User registerToUser(RegisterRequest rq) {
        User user = User.builder()
                        .email(rq.getEmail())
                        .firstname(rq.getFirstname())
                        .lastname(rq.getLastname())
                        .username(rq.getUsername())
                        .password(passwordEncoder.encode(rq.getPassword()))
                        .role(Role.USER)
                        .build();

        return user;
    }


    public Mono<ResponseEntity<AuthResponse>> login(AuthRequest ar) {

        return userRepository.findByEmail(ar.getEmail())
                             .filter(user -> passwordEncoder.matches(ar.getPassword(), user.getPassword()))

                             .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user), user.getUsername(), user.getId(), user.getRole())))
                             .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication error"))));


    }
}
