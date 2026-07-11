package com.nhannkl.jwt_no_oauth2res.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhannkl.jwt_no_oauth2res.model.entity.MyUser;
import com.nhannkl.jwt_no_oauth2res.model.entity.RefreshToken;
import java.util.Optional;



public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByMyUser(MyUser myUser);
}
