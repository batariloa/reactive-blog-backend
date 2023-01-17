package com.batariloa.reactiveblogbackend.config;

import com.batariloa.reactiveblogbackend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationManager  implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(authToken);

        return Mono.just(jwtUtil.validateToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                    List<GrantedAuthority> listOfRoles = Arrays.stream(claims.get("role").toString().split(", "))
                            .map(authority -> new SimpleGrantedAuthority(authority))
                            .collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            listOfRoles
                    );
                });

    }
}
