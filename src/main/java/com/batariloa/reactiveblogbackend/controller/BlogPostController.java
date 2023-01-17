package com.batariloa.reactiveblogbackend.controller;


import com.batariloa.reactiveblogbackend.dto.BlogPostDto;
import com.batariloa.reactiveblogbackend.dto.CreateBlogRequest;
import com.batariloa.reactiveblogbackend.model.BlogPost;
import com.batariloa.reactiveblogbackend.repository.BlogRepository;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/post")

public class BlogPostController {


    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{username}")
    public Flux<BlogPostDto> getUsersPosts(@PathVariable("username") String username) {

        return blogRepository.findAllByOwnerUsername(username)
                             .map(e -> BlogPostDto.builder()
                                                  .id(e.getId())
                                                  .title(e.getTitle())
                                                  .text(e.getText())
                                                  .build());

    }

    @PostMapping
    public Mono<BlogPost> createPost(@RequestBody CreateBlogRequest createBlogRequest, Authentication authentication) {


        return userRepository.findByEmail(authentication.getPrincipal()
                                                        .toString())
                             .flatMap(s -> blogRepository.save(BlogPost.builder()
                                                                       .title(createBlogRequest.getTitle())
                                                                       .text(createBlogRequest.getText())
                                                                       .authorId(s.getId())
                                                                       .ownerId(s.getId())
                                                                       .build()))

                ;

    }


}
