package com.nhannkl.jwt_no_oauth2res.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    
    @GetMapping("/api/profile")
    public String getProfile(Authentication authentication) {
        return authentication.getPrincipal().toString();
    }

}
