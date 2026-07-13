package com.example.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authorization.model.entity.MyUser;
import com.example.authorization.model.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByValue(String value);

    Optional<RefreshToken> findByMyUser(MyUser myUser);
}
