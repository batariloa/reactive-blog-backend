package com.batariloa.reactiveblogbackend.repository;


import com.batariloa.reactiveblogbackend.model.BlogPost;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BlogRepository extends ReactiveCrudRepository<BlogPost, Long> {




    @Query("SELECT * from public.posts p LEFT JOIN public.blogster_user u ON p.owner_id = u.id WHERE u.username=:username ")
    Flux<BlogPost> findAllByOwnerUsername(String username);


}
