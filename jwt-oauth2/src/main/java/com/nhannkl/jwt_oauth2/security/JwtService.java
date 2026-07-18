package com.nhannkl.jwt_oauth2.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.nhannkl.jwt_oauth2.model.entity.MyUser;

@Service
public class JwtService {
    
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final long accessTokenExpiration = 1000 * 60 * 15;
    private final long refreshTokenExpiration = 1000 * 60 * 60 * 24 * 7;
    
    @Autowired
    public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateAccessToken(MyUser myUser) {
        return buildToken(myUser, accessTokenExpiration);
    }

    public String generateRefreshToken(MyUser myUser) {
        return buildToken(myUser, refreshTokenExpiration);
    }

    private String buildToken(MyUser myUser, long expiration) {
        String username = myUser.getUsername();
        String role = myUser.getRole();

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiresAt(now.plusMillis(expiration))
                .build();
        
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String extractUsername(String token) {
        return decode(token).getSubject();
    }

    public String extractRole(String token) {
        return decode(token).getClaimAsString("role");
    }

    private Jwt decode(String token) {
        return jwtDecoder.decode(token);
    }

    public JwtEncoder getJwtEncoder() {
        return jwtEncoder;
    }

    public JwtDecoder getJwtDecoder() {
        return jwtDecoder;
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

}
