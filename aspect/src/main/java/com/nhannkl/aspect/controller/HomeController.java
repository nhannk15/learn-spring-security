package com.nhannkl.aspect.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhannkl.aspect.service.HomeService;

@RestController
public class HomeController {

    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/api/profile")
    public ResponseEntity<Map<String, String>> getProfile() {
        return ResponseEntity.ok().body(homeService.getProfile());
    }

}
