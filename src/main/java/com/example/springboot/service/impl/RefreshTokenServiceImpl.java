package com.example.springboot.service.impl;

import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.RefreshTokenExpiredException;
import com.example.springboot.exception.RefreshTokenNotFoundException;
import com.example.springboot.repository.RefreshTokenRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {


    @Value("${jwt.refresh-token-expiration}")
    private long JWT_REFRESH_TOKEN_VALIDITY;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserProfileRepository userProfileRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserProfileRepository userProfileRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Search the {@link RefreshToken} object by refresh token
     *
     * @param Rtoken : The refresh token
     * @return : The {@link RefreshToken} optional response
     */
    @Override
    public Optional<RefreshToken> findByRefreshToken(String Rtoken) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(Rtoken);
        if (!refreshToken.isPresent()){
            log.error("Refresh token is not in database!");
            throw new RefreshTokenNotFoundException(Rtoken, "Refresh token is not in database!");
        }
        return refreshToken;
    }

    /**
     * Create a new refresh token for user
     *
     * @param userId : The id of the user
     * @return : The {@link RefreshToken} object response
     */
    @Override
    public RefreshToken createRefreshToken(Long userId) {
        log.info("Start create refresh token");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserProfile(userProfileRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plus(JWT_REFRESH_TOKEN_VALIDITY, ChronoUnit.DAYS));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        log.info("End create refresh token");
        return refreshToken;
    }

    /**
     * Check expire of the refresh token
     *  - This method will throw an exception if the refresh token expires
     *
     * @param refreshToken : The {@link RefreshToken} object
     * @return : The {@link RefreshToken} object response
     */
    @Override
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            log.error("Refresh token was expired");
            throw new RefreshTokenExpiredException(refreshToken.getRefreshToken(), "Refresh token was expired. Please make a new signin request");
        }
        return refreshToken;
    }

    /**
     * Remove refresh token of the user
     *
     * @param userProfile : The {@link UserProfile} object
     * @return : The count of deleted rows.
     */
    @Override
    @Transactional
    public int deleteByUserProfile(UserProfile userProfile) {
        return refreshTokenRepository.deleteByUserProfile_UserID(userProfile.getUserID());
    }
}
