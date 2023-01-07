package com.batariloa.reactiveblogbackend.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Data
public class BlogPost {

    @Id
    private Long Id;


}
