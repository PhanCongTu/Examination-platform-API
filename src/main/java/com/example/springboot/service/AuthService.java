package com.example.springboot.service;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.LoginRequestDTO;

public interface AuthService {
    TokenDetails authenticate(LoginRequestDTO loginVM);
}
