package com.batariloa.reactiveblogbackend.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogPostDto {

    private Long id;
    private String text;
    private String title;

}
