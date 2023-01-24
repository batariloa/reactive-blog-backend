package com.batariloa.reactiveblogbackend.config;

import com.batariloa.reactiveblogbackend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
        String accessToken = authentication.getCredentials()
                                           .toString();

        if (accessToken.isEmpty() || accessToken == null) {
            return Mono.just(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));

        }
        if (!jwtUtil.validateToken(accessToken)) {
            return Mono.just(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
        } else {
            String username = jwtUtil.getUsernameFromToken(accessToken);
            Claims claims = jwtUtil.getAllClaimsFromToken(accessToken);
            final Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role")
                                                                                           .toString()
                                                                                           .split(","))
                                                                             .map(SimpleGrantedAuthority::new)
                                                                             .toList();

            return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
        }
    }
}
