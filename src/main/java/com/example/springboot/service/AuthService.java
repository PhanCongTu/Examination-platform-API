package com.example.springboot.service;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.LoginDTO;

public interface AuthService {
    TokenDetails authenticate(LoginDTO loginVM);
}
