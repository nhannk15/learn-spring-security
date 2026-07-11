package com.nhannkl.jwt_no_oauth2res.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long accessTokenExpirationMs = 1000 * 10;
    private final long refreshTokenExpirationMs = 1000 * 60 * 60 * 24 * 7;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, String role, long expiration, String type) {
        String token = Jwts
                .builder()
                .subject(username)
                .claim("role", role)
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey())
                .compact();

        return token;
    }

    public String generateRefreshToken(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        String username = myUserDetails.getUsername();
        String role = myUserDetails.getAuthorities().stream().findFirst().get().getAuthority().toString();

        String refreshToken = generateToken(username, role, refreshTokenExpirationMs, "refreshToken");

        return refreshToken;
    }

    public String generateAccessToken(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        String username = myUserDetails.getUsername();
        String role = myUserDetails.getAuthorities().stream().findFirst().get().getAuthority().toString();

        String accessToken = generateToken(username, role, accessTokenExpirationMs, "accessToken");

        return accessToken;
    }

    public String generateNewAccessToken(String username, String role) {
        return generateToken(username, role, accessTokenExpirationMs, "accessToken");
    }

    public Claims parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean verifyToken(String token) {
        try {
            Claims claims = parseClaim(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return parseClaim(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaim(token).get("role", String.class);
    }

    public String extractType(String token) {
        return parseClaim(token).get("type", String.class);
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

}
