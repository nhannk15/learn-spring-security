package com.nhannkl.jwt_oauth2.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    
    private String username;
    private String password;

}
