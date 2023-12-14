package com.example.springboot.service;

import com.example.springboot.dto.request.*;
import org.springframework.http.ResponseEntity;

public interface UserProfileService {
    ResponseEntity<?> createUser(SignUpDTO signupVM, Boolean isTeacher, Boolean isAdmin);

    ResponseEntity<?> login(LoginDTO loginVM);

    ResponseEntity<?> refreshToken(RefreshTokenDTO refreshTokenDTO);

    ResponseEntity<?> verifyEmail(Long userID, VerificationEmailDTO verificationEmailDTO);

    ResponseEntity<?> resetPassword(String emailAddress, ResetPasswordDTO resetPasswordDTO);

    ResponseEntity<?> changePassword(ChangePasswordDTO changePassword);

    ResponseEntity<?> updateUserProfile(UpdateUserProfileDTO dto);

    ResponseEntity<?> getAllStudentsByStatus(String search, int page, String column, int size, String sortType, boolean isActive);

    ResponseEntity<?> getAllVerifiedStudents(String search, int page, String column, int size, String sortType);

    ResponseEntity<?> getCurrentLoggedInUser();

    ResponseEntity<?> deleteUser(Long userId);
}
