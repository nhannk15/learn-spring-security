package com.example.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authorization.model.entity.MyUser;
import java.util.Optional;


public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
}
