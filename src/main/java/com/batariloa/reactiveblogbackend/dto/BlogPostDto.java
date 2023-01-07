package com.batariloa.reactiveblogbackend.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
public class BlogPostDto {

    private Integer id;
    private String text;
    private String title;

}
