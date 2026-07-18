package com.nhannkl.jwt_oauth2.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtConfiguration {
    
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSecretKey() {
        return new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey secretKey = getSecretKey();
        
        // JWK jwk = new OctetSequenceKey.Builder(secretKey)
        //         .algorithm(JWSAlgorithm.HS256)
        //         .keyID("jwt-key-id")
        //         .build();
        
        // System.out.println("JWK JSON: " + jwk.toJSONString());
        
        // JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return NimbusJwtEncoder.withSecretKey(secretKey).build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

}
