package com.example.springboot.service;

import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.RefreshTokenRequestDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import org.springframework.http.ResponseEntity;

public interface UserProfileService {
    ResponseEntity<?> createUser(SignUpRequestDTO signupVM);

    ResponseEntity<?> login(LoginRequestDTO loginVM);

    ResponseEntity<?> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
}
