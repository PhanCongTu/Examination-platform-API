package com.example.springboot.controller;

import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.RefreshTokenDTO;
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

@Validated
@RestController
@RequestMapping("")
@Slf4j
public class AuthController {
    @Autowired
    private UserProfileService userProfileService;

    @PostMapping(value = "/signup/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> studentSignup(@Valid @RequestBody SignUpRequestDTO signupVM){
        return userProfileService.createUser(signupVM, false);
    }
    @PostMapping(value = "/signup/teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> teacherSignup(@Valid @RequestBody SignUpRequestDTO signupVM){
        return userProfileService.createUser(signupVM, true);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Login(@Valid @RequestBody LoginRequestDTO loginVM){
        return userProfileService.login(loginVM);
    }

    @PostMapping(value = "/refresh_token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> RefreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO){
        return userProfileService.refreshToken(refreshTokenDTO);
    }

    @GetMapping("/check/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> checkRoleStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(String.format("Hello STUDENT %s", auth.getName()), HttpStatus.OK);
    }

    @GetMapping("/check/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> checkRoleTeacher() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(String.format("Hello TEACHER %s", auth.getName()), HttpStatus.OK);
    }
    @GetMapping("/check/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> checkRoleAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(String.format("Hello ADMIN %s", auth.getName()), HttpStatus.OK);
    }

}
