package com.example.springboot.service;

import com.example.springboot.dto.view_model.LoginVM;
import com.example.springboot.dto.view_model.SignupVM;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(SignupVM signupVM);

    ResponseEntity<?> login(LoginVM loginVM);
}
