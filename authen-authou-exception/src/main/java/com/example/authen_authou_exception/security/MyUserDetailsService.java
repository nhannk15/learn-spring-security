package com.example.authen_authou_exception.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.authen_authou_exception.model.entity.MyUser;
import com.example.authen_authou_exception.repository.MyUserRepository;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserRepository myUserRepository;
    public MyUserDetailsService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new MyUserDetails(myUser);
    }
    
}
