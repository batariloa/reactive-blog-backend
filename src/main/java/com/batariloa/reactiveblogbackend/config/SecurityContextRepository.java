package com.batariloa.reactiveblogbackend.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {


    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(SecurityContextRepository.class);

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        String authToken = null;

        if(authHeader!= null && authHeader.startsWith(TOKEN_PREFIX)){
            authToken = authHeader.replace(TOKEN_PREFIX, "");

        } else {
            logger.warn("Couldn't find bearer string, ignoring header.");
        }

        if (authToken!=null){

            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);

            return this.authenticationManager.authenticate(auth)
                    .map( authentication ->
                        new SecurityContextImpl((authentication))
                    );
        }
        return Mono.empty();
    }
}
