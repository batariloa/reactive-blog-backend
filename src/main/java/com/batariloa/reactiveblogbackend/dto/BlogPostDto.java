package com.batariloa.reactiveblogbackend.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPostDto {

    private Integer id;
    private String text;
    private String title;
    private int ownerId;
    private int authorId;
    private boolean repost;
    private String authorUsername;

}
