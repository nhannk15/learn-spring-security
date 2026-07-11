package com.nhannkl.jwt_no_oauth2res.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.nhannkl.jwt_no_oauth2res.model.entity.MyUser;
import com.nhannkl.jwt_no_oauth2res.repository.MyUserRepository;

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
