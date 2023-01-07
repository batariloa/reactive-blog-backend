package com.batariloa.reactiveblogbackend.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Data
public class Post {

    @Id
    private Long Id;


}
