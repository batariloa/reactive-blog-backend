package com.batariloa.reactiveblogbackend.controller;


import com.batariloa.reactiveblogbackend.dto.BlogPostDto;
import com.batariloa.reactiveblogbackend.model.BlogPost;
import com.batariloa.reactiveblogbackend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/post")

public class BlogPostController {


    @Autowired
    private  BlogRepository blogRepository;

    @GetMapping("/{username}")
    public Flux<BlogPostDto> getUsersPosts(@PathVariable("username") String username){

        return blogRepository.findAll()
                .map(e->
                        BlogPostDto
                                .builder()
                                .id(e.getId())
                                .title(e.getTitle())
                                .text(e.getText())
                                .build()
                );

    }


    @PostMapping
    public Mono<BlogPost> test(){

        BlogPost blogPost = BlogPost.builder().title("leeeeeeeeee").text("leeeee").build();
        System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooo");







        return blogRepository.save(blogPost);
    }

}
