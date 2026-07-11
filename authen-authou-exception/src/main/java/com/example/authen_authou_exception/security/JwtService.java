package com.example.authen_authou_exception.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.authen_authou_exception.model.entity.MyUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenExpiry = 1000 * 60 * 15;
    private final long refreshTokenExpiry = 1000 * 60 * 60 * 24 * 7;

    public SecretKey getSecret() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(MyUser myUser, long expiry) {
        Long id = myUser.getId();
        String username = myUser.getUsername();
        String role = myUser.getRole();

        return Jwts.builder()
                .subject(id.toString())
                .claim("role", role)
                .claim("username", username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSecret())
                .compact();
    }

    public String generateAccessToken(MyUser myUser) {
        return generateToken(myUser, accessTokenExpiry);
    }

    public String generateRefreshToken(MyUser myUser) {
        return generateToken(myUser, refreshTokenExpiry);
    }

    public Claims parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSecret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        Claims claims = parseClaim(token);
        return claims.get("username", String.class);
    }

    public String extractRole(String token) {
        Claims claims = parseClaim(token);
        return claims.get("role", String.class);
    }

    public String extractId(String token) {
        Claims claims = parseClaim(token);
        return claims.getSubject();
    }

    public long getAccessTokenExpiry() {
        return accessTokenExpiry;
    }

    public long getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

}
