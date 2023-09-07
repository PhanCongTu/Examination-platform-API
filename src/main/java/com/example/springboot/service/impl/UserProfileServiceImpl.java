package com.example.springboot.service.impl;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.RefreshTokenRequestDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.dto.response.JwtResponseDTO;
import com.example.springboot.dto.response.RefreshTokenResponseDTO;
import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.RefreshTokenNotFoundException;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.service.AuthService;
import com.example.springboot.service.RefreshTokenService;
import com.example.springboot.service.UserProfileService;
import com.example.springboot.util.EnumRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final RefreshTokenService refreshTokenService;

    private final AuthService authService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Create a new user profile and save it into database
     *
     * @param signupVM : The {@link SignUpRequestDTO} object
     * @return : The {@link JwtResponseDTO} responseEntity
     */
    @Override
    @Transactional
    public ResponseEntity<?> createUser(SignUpRequestDTO signupVM) {
        log.info("Start createUser()");
        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setDisplayName(signupVM.getDisplayName());
        newUserProfile.setEmailAddress(signupVM.getEmailAddress());
        newUserProfile.setHashPassword(signupVM.getPassword());
        newUserProfile.setLoginName(signupVM.getLoginName());
        newUserProfile.setEmailAddressVerified(false);
        newUserProfile.setIsEnable(true);
        newUserProfile.setRoles(Arrays.asList(EnumRole.ROLE_USER.name()));
        newUserProfile = userProfileRepository.save(newUserProfile);

        TokenDetails tokenDetails = authService.authenticate(
                new LoginRequestDTO(signupVM.getLoginName(), signupVM.getPassword())
        );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(newUserProfile.getUserID());
        log.info("End createUser()");
        return ResponseEntity.ok(new JwtResponseDTO(
                tokenDetails.getDisplayName(),
                signupVM.getLoginName(),
                signupVM.getEmailAddress(),
                tokenDetails.getAccessToken(),
                refreshToken.getRefreshToken(),
                tokenDetails.getRoles(),
                tokenDetails.getExpired()));
    }

    /**
     * Authenticate the user by username and password
     *
     * @param loginVM : The {@link LoginRequestDTO} object
     * @return : The {@link JwtResponseDTO} response entity
     */
    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginVM) {
        log.info("Start login");
        TokenDetails tokenDetails = authService.authenticate(loginVM);
        Optional<UserProfile> userProfile = userProfileRepository.findOneByLoginName(loginVM.getLoginName());
        if (!userProfile.isPresent()){
            log.error("Can not find user with login name {}", loginVM.getLoginName());
            throw new UsernameNotFoundException(loginVM.getLoginName());
        }
        // Delete the old one before add the new one
        refreshTokenService.deleteByUserProfile(userProfile.get());
        // Add new refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userProfile.get().getUserID());
        log.info("End login");
        return ResponseEntity.ok(new JwtResponseDTO(
                tokenDetails.getDisplayName(),
                loginVM.getLoginName(),
                tokenDetails.getEmailAddress(),
                tokenDetails.getAccessToken(),
                refreshToken.getRefreshToken(),
                tokenDetails.getRoles(),
                tokenDetails.getExpired()));
    }

    /**
     * Renew the old token by the refresh token
     *
     * @param refreshTokenRequestDTO : The {@link RefreshTokenRequestDTO} object
     * @return : The {@link RefreshTokenResponseDTO} responseEntity
     */
    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO){
        log.info("Start refresh token");
        String requestRefreshToken = refreshTokenRequestDTO.getRefreshToken();
        Map<String, Object> claims = new HashMap<>();
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserProfile)
                .map(userProfile -> {
                    String token = jwtTokenProvider.doGenerateToken(claims,userProfile.getLoginName());
                    log.info("End refresh token");
                    return ResponseEntity.ok(new RefreshTokenResponseDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RefreshTokenNotFoundException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
