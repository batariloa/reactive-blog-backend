package com.batariloa.reactiveblogbackend.config;

import com.batariloa.reactiveblogbackend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;


@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtServerAuthenticationConverter.class);

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {


        String authToken = authentication.getCredentials()
                                         .toString();

        logger.warn("AUTH TOKEN IN MANAGER: " + authToken);
        String username = jwtUtil.getUsernameFromToken(authToken);

        return Mono.just(jwtUtil.validateToken(authToken))
                   .filter(valid -> valid)
                   .switchIfEmpty(Mono.empty())
                   .map(valid -> {

                       logger.warn("VALID" + valid);
                       Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                       final Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role")
                                                                                                      .toString()
                                                                                                      .split(","))
                                                                                        .map(SimpleGrantedAuthority::new)
                                                                                        .toList();

                       logger.warn("roles map" + claims.get("role")
                                                       .toString());

                       logger.warn("ISVALID: " + jwtUtil.validateToken(authToken));
                       logger.warn("USERNAME" + username);

                       return new UsernamePasswordAuthenticationToken(username, null, null);
                   });

    }
}
