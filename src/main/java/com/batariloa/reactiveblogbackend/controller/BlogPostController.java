package com.batariloa.reactiveblogbackend.controller;


import com.batariloa.reactiveblogbackend.dto.BlogPostDto;
import com.batariloa.reactiveblogbackend.dto.CreateBlogRequest;
import com.batariloa.reactiveblogbackend.model.BlogPost;
import com.batariloa.reactiveblogbackend.repository.BlogRepository;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class BlogPostController {


    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    @GetMapping
    public Flux<BlogPostDto> getAllPostsForAuthenticatedUser(Authentication authentication) {

        return blogRepository.findAllByOwnerEmailWithAuthorUsername(authentication.getPrincipal()
                                                                                  .toString());

    }

    @GetMapping("/{username}")
    public Flux<BlogPostDto> getUsersPosts(@PathVariable("username") String username) {

        return blogRepository.findAllByOwnerUsernameWithAuthorUsername(username);

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
                                                                       .repost(false)
                                                                       .build()));
    }

    @PostMapping("/repost/{id}")
    public Mono<BlogPost> repostPost(@PathVariable Integer id, Authentication authentication) {

        return userRepository.findByEmail(authentication.getPrincipal()
                                                        .toString())
                             .flatMap(user -> blogRepository.findById(id)
                                                            .map(post -> BlogPost.builder()
                                                                                 .title(post.getTitle())
                                                                                 .text(post.getText())
                                                                                 .ownerId(user.getId())
                                                                                 .authorId(post.getAuthorId())
                                                                                 .repost(true)
                                                                                 .build()))
                             .flatMap(blogRepository::save);
    }

    
}
