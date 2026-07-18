package com.nhannkl.jwt_oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    
    @GetMapping("/api/profile")
    public String getProfile(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }

}
