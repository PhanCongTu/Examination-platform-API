package com.example.springboot.service;

import com.example.springboot.dto.request.*;
import org.springframework.http.ResponseEntity;

public interface UserProfileService {
    ResponseEntity<?> createUser(SignUpRequestDTO signupVM, Boolean isTeacher, Boolean isAdmin);

    ResponseEntity<?> login(LoginRequestDTO loginVM);

    ResponseEntity<?> refreshToken(RefreshTokenDTO refreshTokenDTO);

    ResponseEntity<?> verifyEmail(Long userID, VerificationEmailDTO verificationEmailDTO);

    ResponseEntity<?> resetPassword(String emailAddress, ResetPasswordDTO resetPasswordDTO);

    ResponseEntity<?> changePassword(ChangePasswordDTO changePassword);

    ResponseEntity<?> updateUserProfile(UpdateUserProfileDTO dto);
}
