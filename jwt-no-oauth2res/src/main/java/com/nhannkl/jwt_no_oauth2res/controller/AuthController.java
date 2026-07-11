package com.nhannkl.jwt_no_oauth2res.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhannkl.jwt_no_oauth2res.model.dto.RefreshTokenRequest;
import com.nhannkl.jwt_no_oauth2res.model.entity.MyUser;
import com.nhannkl.jwt_no_oauth2res.model.entity.RefreshToken;
import com.nhannkl.jwt_no_oauth2res.security.JwtService;
import com.nhannkl.jwt_no_oauth2res.security.RefreshTokenService;

@RestController
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/api/refresh-token")
    public String requestNewAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        String request = refreshTokenRequest.getRefreshToken();
        RefreshToken storedRefreshToken = refreshTokenService.validate(request);
        MyUser myUser = storedRefreshToken.getMyUser();
        String newAccessToken = jwtService.generateNewAccessToken(myUser.getUsername(), myUser.getRole());
        
        return newAccessToken;
    }
}
