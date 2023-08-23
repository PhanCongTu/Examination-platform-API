package com.example.springboot.service;

import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByRefreshToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    @Transactional
    int deleteByUserProfile(UserProfile userProfile);
}
