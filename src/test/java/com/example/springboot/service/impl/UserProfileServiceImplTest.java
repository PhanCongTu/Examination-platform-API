package com.example.springboot.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.dto.response.JwtResponseDTO;
import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.service.AuthService;
import com.example.springboot.service.RefreshTokenService;

import java.time.Instant;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.example.springboot.util.EnumRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserProfileServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserProfileServiceImplTest {
    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileServiceImpl userProfileServiceImpl;

    /**
     * Method under test: {@link UserProfileServiceImpl#createUser(SignUpRequestDTO, Boolean)}
     */

    @Test
    void testCreateUser(){
        SignUpRequestDTO signupVM = SignUpRequestDTO.builder()
                .loginName("loginName")
                .password("password")
                .displayName("displayName")
                .emailAddress("testEmail@gmail.com")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .displayName(signupVM.getDisplayName())
                .emailAddress(signupVM.getEmailAddress())
                .hashPassword(passwordEncoder.encode(signupVM.getPassword()))
                .loginName(signupVM.getLoginName())
                .isEmailAddressVerified(false)
                .roles(List.of(EnumRole.ROLE_TEACHER.name()))
                .build();

        TokenDetails tokenDetails = TokenDetails.builder()
                .displayName(signupVM.getDisplayName())
                .roles(List.of(EnumRole.ROLE_TEACHER.name()))
                .accessToken("accessToken")
                .expired(0123)
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken("refreshToken")
                .build();
        when(userProfileRepository.save(Mockito.<UserProfile>any())).thenReturn(userProfile);
        when(authService.authenticate(Mockito.<LoginRequestDTO>any())).thenReturn(tokenDetails);
        when(refreshTokenService.createRefreshToken(Mockito.<Long>any())).thenReturn(refreshToken);

        ResponseEntity<?> actualCreateUserResult =  userProfileServiceImpl.createUser(signupVM, false);
        assertTrue(actualCreateUserResult.hasBody());
        assertTrue(actualCreateUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualCreateUserResult.getStatusCode());
        assertEquals("loginName", ((JwtResponseDTO) actualCreateUserResult.getBody()).getLoginName());
        assertEquals("displayName", ((JwtResponseDTO) actualCreateUserResult.getBody()).getDisplayName());
        assertEquals("testEmail@gmail.com", ((JwtResponseDTO) actualCreateUserResult.getBody()).getEmailAddress());
        assertFalse(((JwtResponseDTO) actualCreateUserResult.getBody()).getIsEmailAddressVerified());
        assertEquals("accessToken", ((JwtResponseDTO) actualCreateUserResult.getBody()).getAccessToken());
        assertEquals("refreshToken", ((JwtResponseDTO) actualCreateUserResult.getBody()).getRefreshToken());
        assertEquals(List.of(EnumRole.ROLE_TEACHER.name()), ((JwtResponseDTO) actualCreateUserResult.getBody()).getRoles());

        verify(userProfileRepository).save(Mockito.<UserProfile>any());
        verify(refreshTokenService).createRefreshToken(Mockito.<Long>any());
        verify(authService).authenticate(Mockito.<LoginRequestDTO>any());


        userProfileServiceImpl.createUser(signupVM, true);
    }

}

