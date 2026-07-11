package com.example.authen_authou_exception.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security,
            AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService,
            MyAuthenticationEntryPoint myAuthenticationEntryPoint, MyAccessDeniedHandler myAccessDeniedHandler)
            throws Exception {

        // --- Custom Filters.
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(
                authenticationManager, jwtService, refreshTokenService);

        MyJwtAuthenticationFilter myJwtAuthenticationFilter = new MyJwtAuthenticationFilter(jwtService);

        // --- SecurityFilterChain Configuration.
        security.csrf((csrf) -> csrf.disable());
        security.authorizeHttpRequests((request) -> request
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/profile").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers("/api/information").hasAuthority("ADMIN")
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
