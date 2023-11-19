package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.*;
import com.example.springboot.dto.response.JwtResponse;
import com.example.springboot.dto.response.TokenResponse;
import com.example.springboot.dto.response.UserProfileResponse;
import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.EmailAddressVerifiedByAnotherUser;
import com.example.springboot.exception.InValidUserStatusException;
import com.example.springboot.exception.RefreshTokenNotFoundException;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.StudentRepositoryRead;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.service.AuthService;
import com.example.springboot.service.MailService;
import com.example.springboot.service.RefreshTokenService;
import com.example.springboot.service.UserProfileService;
import com.example.springboot.util.EnumRole;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final StudentRepositoryRead studentRepositoryRead;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final MailService mailService;
    private final JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;
    private final WebUtils webUtils;
    /**
     * Create a new user profile and save it into database
     *
     * @param signupVM : The {@link SignUpDTO} object
     * @return : The {@link JwtResponse} responseEntity
     */
    @Override
    @Transactional
    public ResponseEntity<?> createUser(SignUpDTO signupVM, Boolean isTeacher, Boolean isAdmin) {
        log.info("Start createUser()");
        UserProfile newUserProfile = new UserProfile();

        // save user information to database
        newUserProfile.setDisplayName(signupVM.getDisplayName());
        newUserProfile.setEmailAddress(signupVM.getEmailAddress());
        newUserProfile.setHashPassword(passwordEncoder.encode(signupVM.getPassword()));
        newUserProfile.setLoginName(signupVM.getLoginName());
        newUserProfile.setIsEmailAddressVerified(false);
        newUserProfile.setIsEnable(true);
        String role = EnumRole.ROLE_STUDENT.name();
        if(isAdmin){
            role = EnumRole.ROLE_ADMIN.name();
        } else if (isTeacher) {
            role = EnumRole.ROLE_TEACHER.name();
        }
        newUserProfile.setRoles(List.of(role));
        newUserProfile = userProfileRepository.save(newUserProfile);

        // create response information to user
        TokenDetails tokenDetails = authService.authenticate(
                new LoginDTO(newUserProfile.getLoginName(), signupVM.getPassword())
        );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(newUserProfile.getUserID());
        log.info("End createUser()");

        return ResponseEntity.ok(new JwtResponse(
                tokenDetails.getDisplayName(),
                signupVM.getLoginName(),
                signupVM.getEmailAddress(),
                newUserProfile.getIsEmailAddressVerified(),
                tokenDetails.getAccessToken(),
                refreshToken.getRefreshToken(),
                tokenDetails.getRoles(),
                tokenDetails.getExpired()));
    }

    /**
     * Authenticate the user by username and password
     *
     * @param loginVM : The {@link LoginDTO} object
     * @return : The {@link JwtResponse} response entity
     */
    @Override
    public ResponseEntity<?> login(LoginDTO loginVM) {
        log.info("Start login");

        // Delete the old refresh before add the new refresh
        Optional<UserProfile> userProfile = userProfileRepository.findOneByLoginNameOrEmailAddressAndIsEmailAddressVerified(loginVM.getLoginName(),loginVM.getLoginName(), true);
        if (userProfile.isEmpty() || !passwordEncoder.matches(loginVM.getPassword(), userProfile.get().getHashPassword())) {
            throw new BadCredentialsException("Wrong login or password.");
        }
        // Delete old refresh token
        refreshTokenService.deleteByUserProfile(userProfile.get());
        // Add new refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userProfile.get().getUserID());

        // get authentication information
        // set login name to loginVm to start create authenticate
        loginVM.setLoginName(userProfile.get().getLoginName());
        TokenDetails tokenDetails = authService.authenticate(loginVM);
        log.info("End login");
        return ResponseEntity.ok(new JwtResponse(
                tokenDetails.getDisplayName(),
                loginVM.getLoginName(),
                tokenDetails.getEmailAddress(),
                userProfile.get().getIsEmailAddressVerified(), 
                tokenDetails.getAccessToken(),
                refreshToken.getRefreshToken(),
                tokenDetails.getRoles(),
                tokenDetails.getExpired()));
    }

    /**
     * Renew the old token by the refresh token
     *
     * @param refreshTokenDTO : The {@link RefreshTokenDTO} object
     * @return : The {@link TokenResponse} responseEntity
     */
    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenDTO refreshTokenDTO) {
        log.info("Start refresh token");
        String requestRefreshToken = refreshTokenDTO.getRefreshToken();
        Map<String, Object> claims = new HashMap<>();

        Optional<RefreshToken> refreshToken = refreshTokenService.findByRefreshToken(requestRefreshToken);
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserProfile)
                .map(userProfile -> {
                    String token = jwtTokenProvider.doGenerateToken(claims, userProfile.getLoginName());
                    log.info("End refresh token");
                    return ResponseEntity.ok(new TokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RefreshTokenNotFoundException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }


    /**
     * Verify email of user by user id
     *
     * @param userID                : ID of user
     * @param verificationEmailDTO: The {@link VerificationEmailDTO}
     * @return : The response entity
     */
    @Override
    public ResponseEntity<?> verifyEmail(Long userID, VerificationEmailDTO verificationEmailDTO) {
        log.info("Start verify email by verification code");
        UserProfile userProfile = userProfileRepository.findById(userID)
                .orElseThrow(UserNotFoundException::new);
        String checkEmailAddress = userProfile.getEmailAddress();

        // If email address has been verified without value of new_email_address column
        if (Objects.isNull(userProfile.getNewEmailAddress()) && userProfile.getIsEmailAddressVerified()) {
            throw new InValidUserStatusException();
        }

        // Make sure that current email address has not been verified by another user
        // If new email address is not null, make sure that this email has not been verified by another user
        if(Objects.nonNull(userProfile.getNewEmailAddress()) && userProfile.getIsEmailAddressVerified()){
            checkEmailAddress = userProfile.getNewEmailAddress();
            // if new email address equals to old verified email address
            //Just for testing, we do not allow users to update the new email address
            // to the same as the old verified email address
            if(userProfile.getEmailAddress().equals(checkEmailAddress)){
                log.error("New email address is the same as value with verified email address : " + checkEmailAddress);
                return ResponseEntity.noContent().build();
            }
        }
        // check email address before verifying
        Optional<UserProfile> value = userProfileRepository
                .findOneByEmailAddressVerified(checkEmailAddress);
        if (value.isPresent()){
            throw new EmailAddressVerifiedByAnotherUser(checkEmailAddress);
        }

        // Check code and update user info.
        if (userProfile.getVerificationCode().equals(verificationEmailDTO.getCode())
                && Instant.now().isBefore(userProfile.getVerificationExpiredCodeTime())) {
            updateVerifiedEmail(userProfile);
            log.info("End verify email by verification code");
            return ResponseEntity.noContent().build();
        } else {
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.VERIFY_NOT_ACCEPTABLE.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.VERIFY_NOT_ACCEPTABLE.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    /**
     * Update information related to verify email after verified
     *
     * @param userProfile : The {@link UserProfile}
     */
    private void updateVerifiedEmail(UserProfile userProfile) {
        userProfile.setIsEmailAddressVerified(true);
        userProfile.setVerificationCode(null);
        userProfile.setVerificationExpiredCodeTime(null);
        if (Objects.nonNull(userProfile.getNewEmailAddress())){
            userProfile.setEmailAddress(userProfile.getNewEmailAddress());
        }
        userProfile.setNewEmailAddress(null);
        userProfileRepository.save(userProfile);
    }

    /**
     * Reset the password with the new password.
     *
     * @param emailAddress     : The user's email address
     * @param resetPasswordDTO : The {@link ResetPasswordDTO}
     * @return : The response entity
     */
    @Override
    public ResponseEntity<?> resetPassword(String emailAddress, ResetPasswordDTO resetPasswordDTO) {
        log.info("Start reset password!");
        UserProfile userProfile = userProfileRepository.findOneByEmailAddressVerified(emailAddress).orElseThrow(
                UserNotFoundException::new
        );
        if (userProfile.getResetPasswordCode().equals(resetPasswordDTO.getCode())
                && Instant.now().isBefore(userProfile.getResetPasswordExpiredCodeTime())) {
            updatePasswordReset(resetPasswordDTO, userProfile);
            log.info("End reset password!");
            return ResponseEntity.noContent().build();
        } else {
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.RESET_PASSWORD_NOT_ACCEPTABLE.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.RESET_PASSWORD_NOT_ACCEPTABLE.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    /**
     * Update information related to reset password after reset password
     *
     * @param resetPasswordDTO : The {@link ResetPasswordDTO}
     * @param userProfile:     The {@link UserProfile}
     */
    private void updatePasswordReset(ResetPasswordDTO resetPasswordDTO, UserProfile userProfile) {
        userProfile.setHashPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        userProfile.setResetPasswordCode(null);
        userProfile.setResetPasswordExpiredCodeTime(null);
        userProfile.setUpdateBy(userProfile.getLoginName());
        userProfile.setUpdateDate(Instant.now());
        userProfileRepository.save(userProfile);
    }

    /**
     * Change password of current logged-in user
     *
     * @param changePassword : The {@link ChangePasswordDTO}
     * @return : The {@link TokenResponse}
     */
    @Override
    public ResponseEntity<?> changePassword(ChangePasswordDTO changePassword) {
        // Get current logged in user
        UserProfile userProfile = webUtils.getCurrentLogedInUser();

        // If the old password is not correct.
        if(!passwordEncoder.matches(changePassword.getOldPassword(), userProfile.getHashPassword())){
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.CHANGE_PASSWORD_WRONG_OLD_PASSWORD.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.CHANGE_PASSWORD_WRONG_OLD_PASSWORD.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        // Update password
        userProfile.setHashPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userProfile.setUpdateBy(userProfile.getLoginName());
        userProfile.setUpdateDate(Instant.now());
        userProfileRepository.save(userProfile);
        // Delete old refresh token
        refreshTokenService.deleteByUserProfile(userProfile);
        // Add new refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userProfile.getUserID());

        // generate a new access token
        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenProvider.doGenerateToken(claims, userProfile.getLoginName());
        log.info("End refresh token");
        return ResponseEntity.ok(new TokenResponse(token, refreshToken.getRefreshToken()));
    }

    /**
     * Update information of current logged-in user
     *
     * @param dto : the DTO from request
     * @return : no content response
     */
    @Override
    public ResponseEntity<?> updateUserProfile(UpdateUserProfileDTO dto) {
        // Get current logged in user
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        // Update display name
        if (Objects.nonNull(dto.getDisplayName())){
            userProfile.setDisplayName(dto.getDisplayName());
            userProfileRepository.save(userProfile);
        }
        // Update email address.
        if (Objects.nonNull(dto.getEmailAddress())){
            String newEmailAddress = dto.getEmailAddress();
            // If old email address has been verified
            if(userProfile.getIsEmailAddressVerified()){
                if(userProfile.getEmailAddress().equals(newEmailAddress)){
                    return ResponseEntity.noContent().build();
                }
                userProfile.setNewEmailAddress(newEmailAddress);
            }
            // If old email address has not been verified
            else {
                userProfile.setEmailAddress(newEmailAddress);
            }
            userProfile.setUpdateBy(userProfile.getLoginName());
            userProfile.setUpdateDate(Instant.now());
            userProfileRepository.save(userProfile);
            mailService.sendVerificationEmail(userProfile.getLoginName());
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> getAllStudentsByStatus(String search, int page, String column, int size, String sortType, boolean isActive) {
        log.info("Start get all active student searched by display name and email");
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search + "%";
        Page<UserProfile> listStudents = studentRepositoryRead
                .findAllSeachedStudentsByStatus(searchText, isActive, pageable);
        Page<UserProfileResponse> response = listStudents
                .map(CustomBuilder::buildUserProfileResponse);
        log.info("Start get all active student searched by display name and email");
        return ResponseEntity.ok(response);
    }
}
