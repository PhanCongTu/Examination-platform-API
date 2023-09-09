package com.example.springboot.controller;

import com.example.springboot.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    MailService mailService;

    @PostMapping(value = "/verify-email")
    public ResponseEntity<?> sendVerificationEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mailService.sendVerificationEmail(auth.getName());
        return ResponseEntity.noContent().build();
    }
}
