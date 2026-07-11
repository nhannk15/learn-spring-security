package com.nhannkl.jwt_no_oauth2res.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    
    private String username;
    private String password;

}
