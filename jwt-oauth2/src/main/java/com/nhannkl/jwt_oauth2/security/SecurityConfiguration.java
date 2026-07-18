package com.nhannkl.jwt_oauth2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security, AuthenticationManager authenticationManager,
            ObjectMapper objectMapper, JwtService jwtService, RefreshTokenService refreshTokenService)
            throws Exception {

        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(
                authenticationManager, objectMapper, jwtService, refreshTokenService);
        MyJwtAuthenticationFilter myJwtAuthenticationFilter = new MyJwtAuthenticationFilter(jwtService);

        security.csrf((csrf) -> csrf.disable());
        security.authorizeHttpRequests((request) -> request
                .requestMatchers("/api/login").permitAll()
                .anyRequest().authenticated());
        security.addFilterAt(myUsernamePasswordAuthenticationFilter, MyUsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(myJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }

}
