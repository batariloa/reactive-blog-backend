package com.batariloa.reactiveblogbackend.repository;


import com.batariloa.reactiveblogbackend.dto.BlogPostDto;
import com.batariloa.reactiveblogbackend.model.BlogPost;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BlogRepository extends ReactiveCrudRepository<BlogPost, Long> {

    @Query("SELECT * from public.posts p LEFT JOIN public.blogster_user u ON p.owner_id = u.id WHERE u.username=:username ")
    Flux<BlogPost> findAllByOwnerUsername(String username);

    @Query("SELECT * from public.posts p LEFT JOIN public.blogster_user u ON p.owner_id = u.id WHERE u.email=:email ")
    Flux<BlogPost> findAllByOwnerEmail(String email);

    @Query("SELECT a.username AS author_username, * from public.posts p LEFT JOIN public.blogster_user u ON p.owner_id = u.id LEFT JOIN public.blogster_user a ON p.author_id = a.id WHERE u.username=:username ")
    Flux<BlogPostDto> findAllByOwnerUsernameWithAuthorUsername(String username);

    @Query("SELECT a.username AS author_username,* from public.posts p LEFT JOIN public.blogster_user u ON p.owner_id = u.id LEFT JOIN public.blogster_user a ON p.author_id = a.id WHERE u.email=:email ")
    Flux<BlogPostDto> findAllByOwnerEmailWithAuthorUsername(String username);

    Mono<BlogPost> findById(Integer id);

}
