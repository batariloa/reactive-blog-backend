package com.batariloa.reactiveblogbackend.service;

import com.batariloa.reactiveblogbackend.dto.MessageResponse;
import com.batariloa.reactiveblogbackend.dto.RegisterRequest;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import com.batariloa.reactiveblogbackend.user.Role;
import com.batariloa.reactiveblogbackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono signUp(Mono<RegisterRequest> request){



     return request
             .map(this::registerToUser)
             .flatMap(user -> userRepository.findByEmail(user.getEmail())
                     .flatMap(user1 -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"error")))
                     .switchIfEmpty(userRepository.save(user))

             );



    }

    public User registerToUser(RegisterRequest rq){
        System.out.println("Daaam bruv");
        User user=       User.builder()
                .email(rq.getEmail())
                .firstname(rq.getFirstname())
                .lastname(rq.getLastname())
                .username(rq.getUsername())
                .password(passwordEncoder.encode(rq.getPassword()))
                .role(Role.USER)
                .build();

        System.out.println("Checking here");
        return user;
    }
}
