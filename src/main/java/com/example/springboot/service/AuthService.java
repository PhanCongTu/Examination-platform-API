package com.example.springboot.service;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.view_model.LoginVM;

public interface AuthService {
    TokenDetails authenticate(LoginVM loginVM);
}
