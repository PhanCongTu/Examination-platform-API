package com.example.springboot.service;

import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface MailService {
    ResponseEntity<?> sendVerificationEmail(String username);

    ResponseEntity<?> sendResetPasswordEmail(String emailAddress);
}
