package com.example.authorization.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.authorization.model.entity.MyUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private final long refreshTokenExpiry = 1000 * 60 * 60 * 24 * 7;
    private final long accessTokenExpiry = 1000 * 15;

    public SecretKey getSecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }

    private String buildToken(MyUser myUser, long expiry) {
        String username = myUser.getUsername();
        String role = myUser.getRole();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .claim("role", role)
                .signWith(getSecret())
                .compact();
    }

    public String generateAccessToken(MyUser myUser) {
        return buildToken(myUser, accessTokenExpiry);
    }

    public String generateRefreshToken(MyUser myUser) {
        return buildToken(myUser, refreshTokenExpiry);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public long getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public long getAccessTokenExpiry() {
        return accessTokenExpiry;
    }

    

}
