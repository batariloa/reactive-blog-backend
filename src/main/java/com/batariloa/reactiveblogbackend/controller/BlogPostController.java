package com.batariloa.reactiveblogbackend.controller;


import com.batariloa.reactiveblogbackend.dto.BlogPostDto;
import com.batariloa.reactiveblogbackend.dto.CreateBlogRequest;
import com.batariloa.reactiveblogbackend.model.BlogPost;
import com.batariloa.reactiveblogbackend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:3000")
public class BlogPostController {


    private final BlogService blogService;
    private static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);

    @GetMapping
    public Flux<BlogPostDto> getAllPostsForAuthenticatedUser(Authentication authentication) {

        return blogService.getAllPostsForUsername(authentication.getPrincipal()
                                                                .toString());
    }

    @GetMapping("/{username}")
    public Flux<BlogPostDto> getUsersPosts(@PathVariable("username") String username) {

        return blogService.getAllPostsForUsername(username);
    }

    @PostMapping
    public Mono<BlogPost> createPost(@RequestBody CreateBlogRequest createBlogRequest, Authentication authentication) {

        return blogService.createPost(authentication.getPrincipal()
                                                    .toString(), createBlogRequest);
    }

    @PostMapping("/repost/{id}")
    public Mono<BlogPost> repostPost(@PathVariable Integer id, Authentication authentication) {

        return blogService.repostPost(id, authentication.getPrincipal()
                                                        .toString());
    }

}
