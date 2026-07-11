package com.example.authen_authou_exception.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.authen_authou_exception.model.entity.MyUser;
import com.example.authen_authou_exception.model.entity.RefreshToken;
import com.example.authen_authou_exception.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalid"));

        if (refreshToken.getExpiryTime().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        return refreshToken;
    }

    public void save(MyUser myUser, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByMyUser(myUser)
                .orElse(new RefreshToken());
        refreshToken.setMyUser(myUser);
        refreshToken.setToken(token);
        refreshToken.setExpiryTime(Instant.now().plusMillis(jwtService.getRefreshTokenExpiry()));
        
        refreshTokenRepository.save(refreshToken);
    }
}
