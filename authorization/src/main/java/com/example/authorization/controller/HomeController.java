package com.example.authorization.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/api/random")
    public String successRandom() {
        return "You are lucky!";
    }

    @GetMapping("/api/time-base")
    public String timebaseAccess() {
        return "You've access ontime!";
    }

}
