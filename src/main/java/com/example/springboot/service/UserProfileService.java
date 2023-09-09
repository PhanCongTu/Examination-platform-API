package com.example.springboot.service;

import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.RefreshTokenRequestDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.dto.request.VerificationEmailDTO;
import org.springframework.http.ResponseEntity;

public interface UserProfileService {
    ResponseEntity<?> createUser(SignUpRequestDTO signupVM);

    ResponseEntity<?> login(LoginRequestDTO loginVM);

    ResponseEntity<?> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);

    ResponseEntity<?> verifyEmail(Long userID, VerificationEmailDTO verificationEmailDTO);
}
