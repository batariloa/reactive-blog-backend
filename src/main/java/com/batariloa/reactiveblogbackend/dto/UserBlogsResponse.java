package com.batariloa.reactiveblogbackend.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserBlogsResponse {

    private List<BlogPostDto> posts;
}
