package com.nhannkl.jwt_oauth2.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhannkl.jwt_oauth2.model.entity.MyUser;
import com.nhannkl.jwt_oauth2.model.entity.RefreshToken;
import com.nhannkl.jwt_oauth2.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    
    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public void save(MyUser myUser, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByMyUser(myUser)
                .orElse(new RefreshToken());
        refreshToken.setMyUser(myUser);
        refreshToken.setValue(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()));

        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByValue(token)
                .orElseThrow(() -> new RuntimeException("RefreshToken hasn't been persisted"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("RefreshToken hasn't been persisted");
        }

        return refreshToken;
    }

}
