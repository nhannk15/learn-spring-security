package com.example.session.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.session.model.entity.MyUser;
import com.example.session.repository.MyUserRepo;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserRepo myUserRepo;

    public MyUserDetailsService(MyUserRepo myUserRepo) {
        this.myUserRepo = myUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = myUserRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return new MyUserDetails(user);
    }
    
}
