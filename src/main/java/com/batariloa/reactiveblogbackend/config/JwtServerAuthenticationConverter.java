package com.batariloa.reactiveblogbackend.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final Logger logger = LoggerFactory.getLogger(JwtServerAuthenticationConverter.class);


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        var tokenPrint = exchange.getRequest()
                                 .getCookies()
                                 .getFirst("token");

        if (tokenPrint == null) {
            return Mono.just(new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")));
        }
        return Mono.justOrEmpty(exchange)
                   .flatMap(e -> Mono.justOrEmpty(e.getRequest()
                                                   .getCookies()
                                                   .getFirst("token")))
                   .filter(Objects::nonNull)
                   .map(token -> new UsernamePasswordAuthenticationToken(token.getValue(), token.getValue()));


    }
}