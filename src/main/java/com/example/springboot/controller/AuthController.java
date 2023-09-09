package com.example.springboot.controller;

import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.RefreshTokenRequestDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("")
@Slf4j
public class AuthController {
    @Autowired
    private UserProfileService userProfileService;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Signup(@Valid @RequestBody SignUpRequestDTO signupVM){
        return userProfileService.createUser(signupVM);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Login(@Valid @RequestBody LoginRequestDTO loginVM){
        return userProfileService.login(loginVM);
    }

    @PostMapping(value = "/refresh_token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> RefreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return userProfileService.refreshToken(refreshTokenRequestDTO);
    }

    @GetMapping("/api/check-role-user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> checkRoleUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(String.format("Hello USER %s", auth.getName()), HttpStatus.OK);
    }

    @GetMapping("/api/check-role-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> checkRoleAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(String.format("Hello ADMIN %s", auth.getName()), HttpStatus.OK);
    }

}
