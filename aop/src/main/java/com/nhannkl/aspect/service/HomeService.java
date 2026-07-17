package com.nhannkl.aspect.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomeService {
    
    public Map<String, String> getProfile() {
        log.info("getProfile()");
        Map<String, String> map = new HashMap<>();
        map.put("username", "nhannk15");
        return map;
    }

}
