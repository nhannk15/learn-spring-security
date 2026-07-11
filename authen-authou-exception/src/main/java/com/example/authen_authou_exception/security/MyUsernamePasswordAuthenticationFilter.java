package com.example.authen_authou_exception.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.authen_authou_exception.model.dto.LoginRequest;
import com.example.authen_authou_exception.model.entity.MyUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, password);

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

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        String responseJson = objectMapper.writeValueAsString(map);

        response.setStatus(200);
        response.setContentType("application/json");
        response.getWriter().write(responseJson);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write("Un-Authenticated");
    }

}
