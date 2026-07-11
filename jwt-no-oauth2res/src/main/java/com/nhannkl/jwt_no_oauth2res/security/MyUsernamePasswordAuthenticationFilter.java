package com.nhannkl.jwt_no_oauth2res.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhannkl.jwt_no_oauth2res.model.dto.LoginRequest;

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
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());
            setDetails(request, usernamePasswordAuthenticationToken);
            return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception ex) {
            throw new AuthenticationServiceException("Can't read Request Body");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        MyUserDetails myUserDetails = (MyUserDetails) authResult.getPrincipal();

        String accessToken = jwtService.generateAccessToken(authResult);
        String refreshToken = jwtService.generateRefreshToken(authResult);

        refreshTokenService.save(myUserDetails.getMyUser(), refreshToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(String.format("RefreshToken: %s, AccessToken: %s", refreshToken, accessToken));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("UN-Authenticated");
        ;
    }

}
