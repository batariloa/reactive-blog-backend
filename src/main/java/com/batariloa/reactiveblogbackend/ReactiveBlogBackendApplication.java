package com.batariloa.reactiveblogbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
public class ReactiveBlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveBlogBackendApplication.class, args);
    }

}
