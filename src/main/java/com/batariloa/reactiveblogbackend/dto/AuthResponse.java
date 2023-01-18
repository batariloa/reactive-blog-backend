package com.batariloa.reactiveblogbackend.dto;


import com.batariloa.reactiveblogbackend.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {

    private String token;
    private String username;
    private int id;
    private Role role;


}
