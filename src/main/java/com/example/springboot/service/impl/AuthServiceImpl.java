package com.example.springboot.service.impl;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.security.JwtUserDetails;
import com.example.springboot.security.UserAuthenticationToken;
import com.example.springboot.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Get {@link TokenDetails} object by username and password
     *
     * @param loginRequestDTO : The {@link LoginRequestDTO} object
     * @return : The {@link TokenDetails} object response
     */
    @Override
    public TokenDetails authenticate(LoginRequestDTO loginRequestDTO){
        log.info("Start create authenticate");
        UserAuthenticationToken authenticationToken = new UserAuthenticationToken(
                loginRequestDTO.getLoginName(),
                loginRequestDTO.getPassword(),
                true
        );
        authenticationManager.authenticate(authenticationToken);
        final JwtUserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginRequestDTO.getLoginName());
        log.info("End create authenticate");
        return jwtTokenProvider.getTokenDetails(userDetails);
    }
}
