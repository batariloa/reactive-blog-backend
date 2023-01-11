package com.batariloa.reactiveblogbackend.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequest {


    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String repeatPassword;
}
