package com.batariloa.reactiveblogbackend.repository;

import com.batariloa.reactiveblogbackend.user.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByEmail(String email);

}
