package com.example.springboot.service;

import com.example.springboot.entity.MultipleChoiceTest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface MailService {
    void sendTestCreatedNotificationEmail(Long classroomId, MultipleChoiceTest multipleChoiceTest);

    void sendTestDeletedNotificationEmail(MultipleChoiceTest multipleChoiceTest);
    void sendTestUpdatedNotificationEmail(MultipleChoiceTest multipleChoiceTest);

    @Async
    ResponseEntity<?> sendVerificationEmail(String username);
    @Async

    ResponseEntity<?> sendResetPasswordEmail(String emailAddress);
}
