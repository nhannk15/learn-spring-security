package com.example.authen_authou_exception.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authen_authou_exception.model.entity.MyUser;
import com.example.authen_authou_exception.repository.MyUserRepository;

@RestController
public class ProfileController {
    
    private final MyUserRepository myUserRepository;
    
    @Autowired
    public ProfileController(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @GetMapping("/api/profile")
    public ResponseEntity<Map<String, String>> getProfile(Authentication authentication) {
        String id = (String) authentication.getPrincipal();
        Long myUserId = Long.parseLong(id);
        MyUser myUser = myUserRepository.findById(myUserId)
                .orElseThrow(() -> new RuntimeException("Wrong id"));
        Map<String, String> response = new HashMap<>();
        response.put("id", myUser.getId().toString());
        response.put("username", myUser.getUsername());
        response.put("role", myUser.getRole());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/information")
    public ResponseEntity<Map<String, String>> getInformation(Authentication authentication) {
        String id = (String) authentication.getPrincipal();
        Long myUserId = Long.parseLong(id);
        MyUser myUser = myUserRepository.findById(myUserId)
                .orElseThrow(() -> new RuntimeException("Wrong id"));
        Map<String, String> response = new HashMap<>();
        response.put("id", myUser.getId().toString());
        response.put("username", myUser.getUsername());
        response.put("role", myUser.getRole());

        return ResponseEntity.ok().body(response);
    }
}
