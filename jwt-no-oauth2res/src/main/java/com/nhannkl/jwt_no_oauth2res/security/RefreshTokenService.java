package com.nhannkl.jwt_no_oauth2res.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhannkl.jwt_no_oauth2res.model.entity.MyUser;
import com.nhannkl.jwt_no_oauth2res.model.entity.RefreshToken;
import com.nhannkl.jwt_no_oauth2res.repository.RefreshTokenRepository;

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

        refreshToken.setRefreshToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()));
        refreshToken.setMyUser(myUser);
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh Token not found"));
        if (refreshToken.isRevoked()) {
            throw new RuntimeException("RefreshToken has been revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("RefreshToken has been revoked");
        }

        return refreshToken;
    }

    public void revoke(String token) {
        refreshTokenRepository.findByRefreshToken(token)
                .ifPresent((refreshToken) -> {
                    refreshToken.setRevoked(true);
                    refreshTokenRepository.save(refreshToken);
                });
    }

}
