package com.example.learning.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.learning.model.entity.MyUser;
import com.example.learning.repository.MyUserRepo;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserRepo myUserRepo;

    @Autowired
    public MyUserDetailsService(MyUserRepo myUserRepo) {
        this.myUserRepo = myUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found: " + username));
        MyUserDetails myUserDetails = new MyUserDetails(myUser);
        return myUserDetails;
    }
    
}
