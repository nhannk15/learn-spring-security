package com.example.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.session.model.entity.MyUser;
import java.util.Optional;


public interface MyUserRepo extends JpaRepository<MyUser, Long> {
    
    Optional<MyUser> findByUsername(String username);

}
