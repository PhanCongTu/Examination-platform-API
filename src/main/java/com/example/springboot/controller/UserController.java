package com.example.springboot.controller;

import com.example.springboot.dto.request.ChangePasswordDTO;
import com.example.springboot.dto.request.ResetPasswordDTO;
import com.example.springboot.dto.request.UpdateUserProfileDTO;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.dto.request.VerificationEmailDTO;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.UserProfileService;

@Validated
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class UserController {

    private UserProfileService userProfileService;
    private MailService mailService;
    private UserProfileRepository userProfileRepository;

    @PostMapping(value = "/email/send-verification")
    public ResponseEntity<?> sendVerificationEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return mailService.sendVerificationEmail(auth.getName());
    }

    @PostMapping(value = "/email/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerificationEmailDTO verificationEmailDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserProfile userProfile = userProfileRepository.findOneByLoginName(auth.getName()).orElseThrow(
                UserNotFoundException::new
        );
        return userProfileService.verifyEmail(userProfile.getUserID(),verificationEmailDTO);
    }

    @PostMapping(value = "/password/request-reset/EMAIL:{email-address}")
    public ResponseEntity<?> sendResetPasswordEmail(@PathVariable("email-address") String emailAddress) {
        return mailService.sendResetPasswordEmail(emailAddress);
    }

    @PostMapping(value = "/password/reset/EMAIL:{email-address}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
                                           @PathVariable(value = "email-address") String emailAddress){
        return userProfileService.resetPassword(emailAddress,resetPasswordDTO);
    }
    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePassword){
        return userProfileService.changePassword(changePassword);
    }
    @PostMapping(value = "/user/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserprofile(@Valid @RequestBody UpdateUserProfileDTO DTO){
        return userProfileService.updateUserProfile(DTO);
    }
}
