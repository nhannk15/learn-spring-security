package com.nhannkl.jwt_no_oauth2res.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhannkl.jwt_no_oauth2res.model.entity.MyUser;


public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
}
