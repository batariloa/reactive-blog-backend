package com.batariloa.reactiveblogbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.session.WebSessionManager;
import reactor.core.publisher.Mono;

import java.util.Arrays;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("X-Auth-Token", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebSessionManager webSessionManager() {
        // Emulate SessionCreationPolicy.STATELESS
        return exchange -> Mono.empty();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtServerAuthenticationConverter jwtServerAuthenticationConverter, AuthenticationManager authenticationManager) {


        AuthenticationWebFilter webFilter = new AuthenticationWebFilter(authenticationManager);
        webFilter.setServerAuthenticationConverter(jwtServerAuthenticationConverter);
        return http

                .exceptionHandling()
                .authenticationEntryPoint((exchange, exception) -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication error" + exception.toString())))
                .accessDeniedHandler((exchange, exception) -> Mono.error(exception))
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .addFilterAt(webFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic()
                .disable()
                .authenticationManager(authenticationManager)
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .authorizeExchange()
                .pathMatchers("/auth/login", "/auth/register", "/auth/search/**", "/auth/refresh-token", "/auth/**")
                .permitAll()
                .pathMatchers("/post", "/post/**", "/post/*")
                .authenticated()
                .and()
                .logout()
                .logoutSuccessHandler(new ServerLogoutSuccessHandler() {
                    @Override
                    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
                        ServerHttpResponse response = exchange.getExchange()
                                                              .getResponse();
                        response.setStatusCode(HttpStatus.FOUND);

                        response.getCookies()
                                .remove("token");
                        return null;
                    }
                })
                .and()
                .build();


    }

}