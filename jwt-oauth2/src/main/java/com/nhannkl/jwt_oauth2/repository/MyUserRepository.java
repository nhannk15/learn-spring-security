package com.nhannkl.jwt_oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhannkl.jwt_oauth2.model.entity.MyUser;
import java.util.Optional;


public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);

}
