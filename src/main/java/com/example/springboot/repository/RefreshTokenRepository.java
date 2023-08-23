package com.example.springboot.repository;

import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    @Modifying
    int deleteByUserProfile(UserProfile userProfile);

    @Modifying
    int deleteByUserProfile_UserID(Long userID);
}
