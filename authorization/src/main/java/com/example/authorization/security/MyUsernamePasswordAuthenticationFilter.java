package com.example.authorization.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.authorization.model.dto.LoginRequest;
import com.example.authorization.model.entity.MyUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
            ObjectMapper objectMapper, RefreshTokenService refreshTokenService, JwtService jwtService) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, usernamePasswordAuthenticationToken);
            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Can't read data from Request Body");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        MyUserDetails myUserDetails = (MyUserDetails) authResult.getPrincipal();
        MyUser myUser = myUserDetails.getMyUser();
        String accessToken = jwtService.generateAccessToken(myUser);
        String refreshToken = jwtService.generateRefreshToken(myUser);
        
        refreshTokenService.save(myUser, refreshToken);
        
        Map<String, Object> map = new HashMap<>();
        map.put("status", HttpServletResponse.SC_OK);
        map.put("message", "Authenticated");
        map.put("accessToken", accessToken);
        map.put("refresToken", refreshToken);
        String json = objectMapper.writeValueAsString(map);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    // @Override
    // protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
    //         AuthenticationException failed) throws IOException, ServletException {
    //     Map<String, Object> map = new HashMap<>();
    //     map.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    //     map.put("message", "Unauthenticated");
    //     String json = objectMapper.writeValueAsString(map);

    //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //     response.setContentType("application/json");
    //     response.getWriter().write(json);
    // }
    
}
