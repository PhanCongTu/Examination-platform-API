package com.example.springboot.controller;

import com.example.springboot.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.dto.request.VerificationEmailDTO;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.UserProfileService;

@Validated
@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileRepository userProfileRepository;
    @PostMapping(value = "/email/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerificationEmailDTO verificationEmailDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserProfile userProfile = userProfileRepository.findOneByLoginName(auth.getName()).orElseThrow(
                UserNotFoundException::new
        );
        return userProfileService.verifyEmail(userProfile.getUserID(),verificationEmailDTO);
    }
}
