package com.example.authorization.security;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security, ObjectMapper objectMapper,
            AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService)
            throws Exception {

        // --- Custom Filters.
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(
                authenticationManager, objectMapper, refreshTokenService, jwtService);
        MyJwtAuthenticationFilter myJwtAuthenticationFilter = new MyJwtAuthenticationFilter(jwtService);

        // --- Customer SpringSecurity Exception Handlers
        MyAccessDeniedHandler myAccessDeniedHandler = new MyAccessDeniedHandler();
        MyAuthenticationEntryPoint myAuthenticationEntryPoint = new MyAuthenticationEntryPoint();

        // --- SecurityFilterChain Configuration
        security.csrf((csrf) -> csrf.disable());
        security.authorizeHttpRequests((request) -> request
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/random").access((authentication, context) -> {
                    boolean randomAllow = new Random().nextBoolean();
                    return new AuthorizationDecision(randomAllow);
                })
                .anyRequest().authenticated());
        security.addFilterAt(myUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(myJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        security.exceptionHandling((exception) -> exception
                .accessDeniedHandler(myAccessDeniedHandler)
                .authenticationEntryPoint(myAuthenticationEntryPoint)
        );

        return security.build();
    }

}
