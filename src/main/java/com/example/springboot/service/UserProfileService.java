package com.example.springboot.service;

import com.example.springboot.dto.request.*;
import org.springframework.http.ResponseEntity;

public interface UserProfileService {
    ResponseEntity<?> createUser(SignUpRequestDTO signupVM);

    ResponseEntity<?> login(LoginRequestDTO loginVM);

    ResponseEntity<?> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);

    ResponseEntity<?> verifyEmail(Long userID, VerificationEmailDTO verificationEmailDTO);

    ResponseEntity<?> resetPassword(String emailAddress, ResetPasswordDTO resetPasswordDTO);
}
