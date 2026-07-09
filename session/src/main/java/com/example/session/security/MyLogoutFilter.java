package com.example.session.security;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class MyLogoutFilter extends LogoutFilter {

    public MyLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
        super(logoutSuccessHandler, handlers);
        setFilterProcessesUrl("/api/logout");
    }
    
}
