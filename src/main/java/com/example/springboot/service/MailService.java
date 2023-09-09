package com.example.springboot.service;

import java.util.concurrent.CompletableFuture;

public interface MailService {
    void sendVerificationEmail(String username);
}
