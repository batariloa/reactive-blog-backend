package com.batariloa.reactiveblogbackend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("public.posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPost {

    @Id
    private Integer Id;

    @Column
    private String text;

    @Column
    private String title;

    @Column
    private Integer authorId;

    @Column
    private Integer ownerId;



}
