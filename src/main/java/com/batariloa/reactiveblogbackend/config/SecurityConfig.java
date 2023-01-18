package com.batariloa.reactiveblogbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurerComposite() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        return http.cors()
                   .and()

                   .exceptionHandling()
                   .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
                                                                                    .setStatusCode(HttpStatus.UNAUTHORIZED)))
                   .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
                                                                               .setStatusCode(HttpStatus.FORBIDDEN)))
                   .and()
                   .csrf()
                   .disable()
                   .formLogin()
                   .disable()
                   .httpBasic()
                   .disable()
                   .authenticationManager(authenticationManager)
                   .securityContextRepository(securityContextRepository)
                   .authorizeExchange()
                   .pathMatchers(HttpMethod.OPTIONS)
                   .permitAll()
                   .pathMatchers("/auth/login", "/auth/register")
                   .permitAll()
                   .anyExchange()
                   .authenticated()
                   .and()

                   .build();

    }


}
