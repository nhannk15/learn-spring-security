package com.example.authorization.security;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.example.authorization.model.entity.MyUser;
import com.example.authorization.model.entity.RefreshToken;
import com.example.authorization.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public void save(MyUser myUser, String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByMyUser(myUser)
                .orElse(new RefreshToken());
        refreshToken.setValue(token);
        refreshToken.setMyUser(myUser);
        refreshToken.setExpiration(Instant.now().plusMillis(jwtService.getRefreshTokenExpiry()));
        refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByValue(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        if (refreshToken.getExpiration().isBefore(Instant.now())) {
            throw new RuntimeException("Token has expired");
        }

        return refreshToken;
    }

}
