package com.nhannkl.jwt_oauth2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhannkl.jwt_oauth2.model.entity.MyUser;
import com.nhannkl.jwt_oauth2.model.entity.RefreshToken;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByMyUser(MyUser myUser);
    Optional<RefreshToken> findByValue(String value);

}
