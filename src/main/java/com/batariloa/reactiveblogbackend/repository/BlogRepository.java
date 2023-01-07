package com.batariloa.reactiveblogbackend.repository;


import com.batariloa.reactiveblogbackend.model.BlogPost;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BlogRepository extends ReactiveCrudRepository<BlogPost, Long> {
}
