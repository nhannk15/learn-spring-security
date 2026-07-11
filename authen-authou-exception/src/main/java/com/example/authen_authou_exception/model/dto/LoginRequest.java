package com.example.authen_authou_exception.model.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
