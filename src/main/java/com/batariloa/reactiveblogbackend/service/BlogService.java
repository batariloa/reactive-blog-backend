package com.batariloa.reactiveblogbackend.service;

import com.batariloa.reactiveblogbackend.dto.BlogPostDto;
import com.batariloa.reactiveblogbackend.dto.CreateBlogRequest;
import com.batariloa.reactiveblogbackend.model.BlogPost;
import com.batariloa.reactiveblogbackend.repository.BlogRepository;
import com.batariloa.reactiveblogbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BlogService {

    public final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public Mono<BlogPost> createPost(String username, CreateBlogRequest createBlogRequest) {
        return userRepository.findByUsername(username)
                             .flatMap(s -> blogRepository.save(BlogPost.builder()
                                                                       .title(createBlogRequest.getTitle())
                                                                       .text(createBlogRequest.getText())
                                                                       .authorId(s.getId())
                                                                       .ownerId(s.getId())
                                                                       .repost(false)
                                                                       .build()));
    }

    public Mono<BlogPost> repostPost(int id, String username) {

        return userRepository.findByUsername(username)
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

    public Flux<BlogPostDto> getAllPostsForUsername(String username) {
        return blogRepository.findAllByOwnerUsernameWithAuthorUsername(username);

    }
}
