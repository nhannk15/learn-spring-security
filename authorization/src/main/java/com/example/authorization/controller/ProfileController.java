package com.example.authorization.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/api/profile")
    public String getProfile(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/api/information")
    public String getInformation(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }


}
