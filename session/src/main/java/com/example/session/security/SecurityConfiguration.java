package com.example.session.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

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
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security, AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository)
            throws Exception {
        //--- My custom filter classes.
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter(
                authenticationManager, securityContextRepository);

        LogoutHandler securityHandler = new SecurityContextLogoutHandler();
        LogoutHandler cookieHandler = new CookieClearingLogoutHandler("JSESSIONID");
        LogoutSuccessHandler logoutSuccessHandler = new MyLogoutSuccessHandler();
        MyLogoutFilter myLogoutFilter = new MyLogoutFilter(logoutSuccessHandler, securityHandler, cookieHandler);

        //--- The SecurityFilterChain Configuration.
        security.csrf((csrf) -> csrf.disable());
        security.securityContext(
                (securityContext) -> securityContext.securityContextRepository(securityContextRepository));
        security.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        security.authorizeHttpRequests((request) -> request
                .requestMatchers("/api/login", "/api/logout").permitAll()
                .anyRequest().authenticated());
        security.addFilterAt(myUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        security.addFilterAt(myLogoutFilter, LogoutFilter.class);

        return security.build();
    }

}
