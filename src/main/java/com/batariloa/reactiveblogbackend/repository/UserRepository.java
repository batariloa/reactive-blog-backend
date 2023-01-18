package com.batariloa.reactiveblogbackend.repository;

import com.batariloa.reactiveblogbackend.dto.SearchUserDto;
import com.batariloa.reactiveblogbackend.user.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByEmail(String email);

    @Query("select * from public.blogster_user WHERE username LIKE '%'||:username||'%' LIMIT 6")
    Flux<SearchUserDto> searchUsersByUsername(String username);


}
