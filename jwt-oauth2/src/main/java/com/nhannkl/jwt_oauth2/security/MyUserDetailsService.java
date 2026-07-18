package com.nhannkl.jwt_oauth2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nhannkl.jwt_oauth2.model.entity.MyUser;
import com.nhannkl.jwt_oauth2.repository.MyUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MyUserRepository myUserRepository;

    @Autowired
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
