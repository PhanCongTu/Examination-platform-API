package com.example.springboot.service.impl;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.security.JwtUserDetails;
import com.example.springboot.security.UserAuthenticationToken;
import com.example.springboot.service.AuthService;
import com.example.springboot.dto.view_model.LoginVM;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public TokenDetails authenticate(LoginVM loginVM){
        UserAuthenticationToken authenticationToken = new UserAuthenticationToken(
                loginVM.getLoginName(),
                loginVM.getPassword(),
                true
        );
        authenticationManager.authenticate(authenticationToken);
        final JwtUserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginVM.getLoginName());

        final TokenDetails result = jwtTokenProvider.getTokenDetails(userDetails, null);
        return result;
    }
}
