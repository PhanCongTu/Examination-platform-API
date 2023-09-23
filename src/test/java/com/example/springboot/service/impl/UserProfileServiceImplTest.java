package com.example.springboot.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.request.LoginRequestDTO;
import com.example.springboot.dto.request.RefreshTokenRequestDTO;
import com.example.springboot.dto.request.ResetPasswordDTO;
import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.dto.request.VerificationEmailDTO;
import com.example.springboot.dto.response.JwtResponseDTO;
import com.example.springboot.dto.response.RefreshTokenResponseDTO;
import com.example.springboot.entity.RefreshToken;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.EmailAddressVerifiedByAnotherUser;
import com.example.springboot.exception.InValidUserStatusException;
import com.example.springboot.exception.RefreshTokenNotFoundException;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.security.JwtTokenProvider;
import com.example.springboot.service.AuthService;
import com.example.springboot.service.MailService;
import com.example.springboot.service.RefreshTokenService;

import java.time.Instant;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.example.springboot.util.EnumRole;

import java.util.Map;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

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
    private MailService mailService;

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
    void testCreateUser() {
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

        ResponseEntity<?> actualCreateUserResult = userProfileServiceImpl.createUser(signupVM, false);
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

    /**
     * Method under test: {@link UserProfileServiceImpl#login(LoginRequestDTO)}
     */
    @Test
    void testLogin() {
        LoginRequestDTO loginVM = LoginRequestDTO.builder()
                .loginName("loginName")
                .password("password")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .userID(1L)
                .loginName("loginName")
                .displayName("displayName")
                .isEmailAddressVerified(false)
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken("refreshToken")
                .build();
        TokenDetails tokenDetails = TokenDetails.builder()
                .displayName("displayName")
                .emailAddress("testEmail@gmail.com")
                .roles(List.of(EnumRole.ROLE_TEACHER.name()))
                .accessToken("accessToken")
                .expired(0123)
                .build();
        when(userProfileRepository.findOneByLoginNameOrEmailAddressAndIsEmailAddressVerified(Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<Boolean>any())).thenReturn(Optional.of(userProfile));
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        when(refreshTokenService.deleteByUserProfile(Mockito.<UserProfile>any())).thenReturn(1);
        when(refreshTokenService.createRefreshToken(Mockito.<Long>any())).thenReturn(refreshToken);
        when(authService.authenticate(Mockito.<LoginRequestDTO>any())).thenReturn(tokenDetails);

        ResponseEntity<?> actualCreateUserResult = userProfileServiceImpl.login(loginVM);
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

        verify(userProfileRepository).findOneByLoginNameOrEmailAddressAndIsEmailAddressVerified(Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<Boolean>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
        verify(refreshTokenService).deleteByUserProfile(Mockito.<UserProfile>any());
        verify(refreshTokenService).createRefreshToken(Mockito.<Long>any());
        verify(authService).authenticate(Mockito.<LoginRequestDTO>any());

        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(false);
        assertThrows(BadCredentialsException.class,
                () -> userProfileServiceImpl.login(loginVM));
        when(userProfileRepository.findOneByLoginNameOrEmailAddressAndIsEmailAddressVerified(Mockito.<String>any(),
                Mockito.<String>any(), Mockito.<Boolean>any())).thenReturn(Optional.empty());
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        assertThrows(BadCredentialsException.class,
                () -> userProfileServiceImpl.login(loginVM));
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#refreshToken(RefreshTokenRequestDTO)}
     */
    @Test
    void testRefreshToken() {
        UserProfile userProfile = UserProfile.builder()
                .userID(1L)
                .loginName("loginName")
                .displayName("displayName")
                .isEmailAddressVerified(false)
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken("refreshToken")
                .userProfile(userProfile)
                .build();
        when(refreshTokenService.findByRefreshToken(Mockito.<String>any())).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(Mockito.<RefreshToken>any())).thenReturn(refreshToken);
        when(jwtTokenProvider.doGenerateToken(Mockito.<Map<String, Object>>any(), Mockito.<String>any()))
                .thenReturn("accessToken");
        ResponseEntity<?> actualRefreshTokenResult = userProfileServiceImpl
                .refreshToken(new RefreshTokenRequestDTO("refreshToken"));

        assertTrue(actualRefreshTokenResult.hasBody());
        assertTrue(actualRefreshTokenResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualRefreshTokenResult.getStatusCode());
        assertEquals("accessToken", ((RefreshTokenResponseDTO) actualRefreshTokenResult.getBody()).getAccessToken());
        assertEquals("refreshToken", ((RefreshTokenResponseDTO) actualRefreshTokenResult.getBody()).getRefreshToken());
        assertEquals("Bearer", ((RefreshTokenResponseDTO) actualRefreshTokenResult.getBody()).getTokenType());

        verify(refreshTokenService).verifyExpiration(Mockito.<RefreshToken>any());
        verify(refreshTokenService, atLeast(1)).findByRefreshToken(Mockito.<String>any());
        verify(jwtTokenProvider).doGenerateToken(Mockito.<Map<String, Object>>any(), Mockito.<String>any());

        when(refreshTokenService.findByRefreshToken(Mockito.<String>any())).thenReturn(Optional.empty());
        assertThrows(RefreshTokenNotFoundException.class,
                () -> userProfileServiceImpl.refreshToken(new RefreshTokenRequestDTO("refreshToken")));

    }

    /**
     * Method under test: {@link UserProfileServiceImpl#verifyEmail(Long, VerificationEmailDTO)}
     */
    @Test
    void testVerifyEmail() {
        UserProfile userProfile = UserProfile.builder()
                .userID(1L)
                .loginName("loginName")
                .displayName("displayName")
                .emailAddress("testEmail@gmail.com")
                .newEmailAddress("newEmail@gmail.com")
                .verificationCode("code")
                .verificationExpiredCodeTime(Instant.now().plusSeconds(5))
                .isEmailAddressVerified(true)
                .build();
        when(userProfileRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(userProfile));
        userProfileServiceImpl.verifyEmail(1L, new VerificationEmailDTO("code"));

        userProfile.setVerificationCode("code");
        userProfile.setVerificationExpiredCodeTime(Instant.now().plusSeconds(5));
        userProfile.setNewEmailAddress(null);
        userProfile.setIsEmailAddressVerified(false);
        when(userProfileRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(userProfile));
        userProfileServiceImpl.verifyEmail(1L, new VerificationEmailDTO("123"));


        ResponseEntity<?> actualVerifyEmailResult = userProfileServiceImpl.verifyEmail(1L, new VerificationEmailDTO("code"));
        assertNull(actualVerifyEmailResult.getBody());
        assertEquals(HttpStatus.NO_CONTENT, actualVerifyEmailResult.getStatusCode());
        assertTrue(actualVerifyEmailResult.getHeaders().isEmpty());

        userProfile.setEmailAddress(null);
        assertThrows(InValidUserStatusException.class,
                () -> userProfileServiceImpl.verifyEmail(1L, new VerificationEmailDTO("code")));

        userProfile.setEmailAddress("testEmail@gmail.com");
        userProfile.setNewEmailAddress(userProfile.getEmailAddress());
        ResponseEntity<?> actualVerifyEmailResult2 = userProfileServiceImpl.verifyEmail(1L, new VerificationEmailDTO("code"));
        assertNull(actualVerifyEmailResult2.getBody());
        assertEquals(HttpStatus.NO_CONTENT, actualVerifyEmailResult2.getStatusCode());
        assertTrue(actualVerifyEmailResult2.getHeaders().isEmpty());


        userProfile.setIsEmailAddressVerified(false);
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.anyString())).thenReturn(Optional.of(userProfile));
        assertThrows(EmailAddressVerifiedByAnotherUser.class,
                () -> userProfileServiceImpl.verifyEmail(1L, new VerificationEmailDTO("code")));

    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    void testResetPassword() {
        UserProfile userProfile = UserProfile.builder()
                .userID(1L)
                .loginName("loginName")
                .displayName("displayName")
                .emailAddress("testEmail@gmail.com")
                .newEmailAddress("newEmail@gmail.com")
                .resetPasswordCode("code")
                .resetPasswordExpiredCodeTime(Instant.now().plusSeconds(5))
                .isEmailAddressVerified(true)
                .build();
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(Optional.of(userProfile));

        ResponseEntity<?> actualResetPasswordResult = userProfileServiceImpl.resetPassword("testEmail@gmail.com",
                new ResetPasswordDTO("iloveyou", "code"));
        assertFalse(actualResetPasswordResult.hasBody());
        assertEquals(HttpStatus.NO_CONTENT, actualResetPasswordResult.getStatusCode());
        verify(userProfileRepository).findOneByEmailAddressVerified(Mockito.<String>any());

        userProfile.setResetPasswordCode("Code");
        userProfile.setResetPasswordExpiredCodeTime(Instant.now().minusSeconds(5));
        userProfileServiceImpl.resetPassword("testEmail@gmail.com",
                new ResetPasswordDTO("iloveyou", "wrong"));

        userProfile.setResetPasswordCode("Code");
        userProfile.setResetPasswordExpiredCodeTime(Instant.now().minusSeconds(5));
        userProfileServiceImpl.resetPassword("testEmail@gmail.com",
                new ResetPasswordDTO("iloveyou", "Code"));
        userProfile.setResetPasswordCode("Code");
        userProfile.setResetPasswordExpiredCodeTime(Instant.now());
        userProfileServiceImpl.resetPassword("testEmail@gmail.com",
                new ResetPasswordDTO("iloveyou", "Code"));
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    void testResetPassword2() {
        UserProfile userProfile = new UserProfile();
        userProfile.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        userProfile.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setDisplayName("Display Name");
        userProfile.setEmailAddress("42 Main St");
        userProfile.setHashPassword("iloveyou");
        userProfile.setIsEmailAddressVerified(true);
        userProfile.setIsEnable(true);
        userProfile.setLoginName("Login Name");
        userProfile.setNewEmailAddress("42 Main St");
        userProfile.setRefreshTokens(new ArrayList<>());
        userProfile.setResetPasswordCode("Reset Password Code");
        userProfile
                .setResetPasswordExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setRoles(new ArrayList<>());
        userProfile.setUpdateBy("2020-03-01");
        userProfile.setUpdateDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setUserID(1L);
        userProfile.setVerificationCode("Verification Code");
        userProfile
                .setVerificationExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Optional<UserProfile> ofResult = Optional.of(userProfile);
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(ofResult);
        ResponseEntity<?> actualResetPasswordResult = userProfileServiceImpl.resetPassword("42 Main St",
                new ResetPasswordDTO("iloveyou", "Code"));
        assertEquals(2, ((Map<String, String>) actualResetPasswordResult.getBody()).size());
        assertTrue(actualResetPasswordResult.hasBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, actualResetPasswordResult.getStatusCode());
        assertEquals(1, actualResetPasswordResult.getHeaders().size());
        verify(userProfileRepository).findOneByEmailAddressVerified(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    void testResetPassword3() {
        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getResetPasswordCode()).thenReturn("Reset Password Code");
        doNothing().when(userProfile).setCreatedBy(Mockito.<String>any());
        doNothing().when(userProfile).setCreatedDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setIsEnable(Mockito.<Boolean>any());
        doNothing().when(userProfile).setUpdateBy(Mockito.<String>any());
        doNothing().when(userProfile).setUpdateDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setDisplayName(Mockito.<String>any());
        doNothing().when(userProfile).setEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setHashPassword(Mockito.<String>any());
        doNothing().when(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        doNothing().when(userProfile).setLoginName(Mockito.<String>any());
        doNothing().when(userProfile).setNewEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        doNothing().when(userProfile).setResetPasswordCode(Mockito.<String>any());
        doNothing().when(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        doNothing().when(userProfile).setRoles(Mockito.<List<String>>any());
        doNothing().when(userProfile).setUserID(Mockito.<Long>any());
        doNothing().when(userProfile).setVerificationCode(Mockito.<String>any());
        doNothing().when(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
        userProfile.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        userProfile.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setDisplayName("Display Name");
        userProfile.setEmailAddress("42 Main St");
        userProfile.setHashPassword("iloveyou");
        userProfile.setIsEmailAddressVerified(true);
        userProfile.setIsEnable(true);
        userProfile.setLoginName("Login Name");
        userProfile.setNewEmailAddress("42 Main St");
        userProfile.setRefreshTokens(new ArrayList<>());
        userProfile.setResetPasswordCode("Reset Password Code");
        userProfile
                .setResetPasswordExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setRoles(new ArrayList<>());
        userProfile.setUpdateBy("2020-03-01");
        userProfile.setUpdateDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setUserID(1L);
        userProfile.setVerificationCode("Verification Code");
        userProfile
                .setVerificationExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Optional<UserProfile> ofResult = Optional.of(userProfile);
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(ofResult);
        ResponseEntity<?> actualResetPasswordResult = userProfileServiceImpl.resetPassword("42 Main St",
                new ResetPasswordDTO("iloveyou", "Code"));
        assertEquals(2, ((Map<String, String>) actualResetPasswordResult.getBody()).size());
        assertTrue(actualResetPasswordResult.hasBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, actualResetPasswordResult.getStatusCode());
        assertEquals(1, actualResetPasswordResult.getHeaders().size());
        verify(userProfileRepository).findOneByEmailAddressVerified(Mockito.<String>any());
        verify(userProfile).getResetPasswordCode();
        verify(userProfile).setCreatedBy(Mockito.<String>any());
        verify(userProfile).setCreatedDate(Mockito.<Instant>any());
        verify(userProfile).setIsEnable(Mockito.<Boolean>any());
        verify(userProfile).setUpdateBy(Mockito.<String>any());
        verify(userProfile).setUpdateDate(Mockito.<Instant>any());
        verify(userProfile).setDisplayName(Mockito.<String>any());
        verify(userProfile).setEmailAddress(Mockito.<String>any());
        verify(userProfile).setHashPassword(Mockito.<String>any());
        verify(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        verify(userProfile).setLoginName(Mockito.<String>any());
        verify(userProfile).setNewEmailAddress(Mockito.<String>any());
        verify(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        verify(userProfile).setResetPasswordCode(Mockito.<String>any());
        verify(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        verify(userProfile).setRoles(Mockito.<List<String>>any());
        verify(userProfile).setUserID(Mockito.<Long>any());
        verify(userProfile).setVerificationCode(Mockito.<String>any());
        verify(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    void testResetPassword4() {
        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getResetPasswordExpiredCodeTime())
                .thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(userProfile.getResetPasswordCode()).thenReturn("Code");
        doNothing().when(userProfile).setCreatedBy(Mockito.<String>any());
        doNothing().when(userProfile).setCreatedDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setIsEnable(Mockito.<Boolean>any());
        doNothing().when(userProfile).setUpdateBy(Mockito.<String>any());
        doNothing().when(userProfile).setUpdateDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setDisplayName(Mockito.<String>any());
        doNothing().when(userProfile).setEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setHashPassword(Mockito.<String>any());
        doNothing().when(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        doNothing().when(userProfile).setLoginName(Mockito.<String>any());
        doNothing().when(userProfile).setNewEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        doNothing().when(userProfile).setResetPasswordCode(Mockito.<String>any());
        doNothing().when(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        doNothing().when(userProfile).setRoles(Mockito.<List<String>>any());
        doNothing().when(userProfile).setUserID(Mockito.<Long>any());
        doNothing().when(userProfile).setVerificationCode(Mockito.<String>any());
        doNothing().when(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
        userProfile.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        userProfile.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setDisplayName("Display Name");
        userProfile.setEmailAddress("42 Main St");
        userProfile.setHashPassword("iloveyou");
        userProfile.setIsEmailAddressVerified(true);
        userProfile.setIsEnable(true);
        userProfile.setLoginName("Login Name");
        userProfile.setNewEmailAddress("42 Main St");
        userProfile.setRefreshTokens(new ArrayList<>());
        userProfile.setResetPasswordCode("Reset Password Code");
        userProfile
                .setResetPasswordExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setRoles(new ArrayList<>());
        userProfile.setUpdateBy("2020-03-01");
        userProfile.setUpdateDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setUserID(1L);
        userProfile.setVerificationCode("Verification Code");
        userProfile
                .setVerificationExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Optional<UserProfile> ofResult = Optional.of(userProfile);
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(ofResult);
        ResponseEntity<?> actualResetPasswordResult = userProfileServiceImpl.resetPassword("42 Main St",
                new ResetPasswordDTO("iloveyou", "Code"));
        assertEquals(2, ((Map<String, String>) actualResetPasswordResult.getBody()).size());
        assertTrue(actualResetPasswordResult.hasBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, actualResetPasswordResult.getStatusCode());
        assertEquals(1, actualResetPasswordResult.getHeaders().size());
        verify(userProfileRepository).findOneByEmailAddressVerified(Mockito.<String>any());
        verify(userProfile).getResetPasswordCode();
        verify(userProfile).getResetPasswordExpiredCodeTime();
        verify(userProfile).setCreatedBy(Mockito.<String>any());
        verify(userProfile).setCreatedDate(Mockito.<Instant>any());
        verify(userProfile).setIsEnable(Mockito.<Boolean>any());
        verify(userProfile).setUpdateBy(Mockito.<String>any());
        verify(userProfile).setUpdateDate(Mockito.<Instant>any());
        verify(userProfile).setDisplayName(Mockito.<String>any());
        verify(userProfile).setEmailAddress(Mockito.<String>any());
        verify(userProfile).setHashPassword(Mockito.<String>any());
        verify(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        verify(userProfile).setLoginName(Mockito.<String>any());
        verify(userProfile).setNewEmailAddress(Mockito.<String>any());
        verify(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        verify(userProfile).setResetPasswordCode(Mockito.<String>any());
        verify(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        verify(userProfile).setRoles(Mockito.<List<String>>any());
        verify(userProfile).setUserID(Mockito.<Long>any());
        verify(userProfile).setVerificationCode(Mockito.<String>any());
        verify(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testResetPassword5() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.example.springboot.exception.UserNotFoundException
        //       at java.util.Optional.orElseThrow(Optional.java:403)
        //       at com.example.springboot.service.impl.UserProfileServiceImpl.resetPassword(UserProfileServiceImpl.java:237)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(Optional.empty());
        userProfileServiceImpl.resetPassword("42 Main St", new ResetPasswordDTO("iloveyou", "Code"));
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testResetPassword6() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "com.example.springboot.dto.request.ResetPasswordDTO.getCode()" because "resetPasswordDTO" is null
        //       at com.example.springboot.service.impl.UserProfileServiceImpl.resetPassword(UserProfileServiceImpl.java:240)
        //   See https://diff.blue/R013 to resolve this issue.

        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getResetPasswordExpiredCodeTime())
                .thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(userProfile.getResetPasswordCode()).thenReturn("Code");
        doNothing().when(userProfile).setCreatedBy(Mockito.<String>any());
        doNothing().when(userProfile).setCreatedDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setIsEnable(Mockito.<Boolean>any());
        doNothing().when(userProfile).setUpdateBy(Mockito.<String>any());
        doNothing().when(userProfile).setUpdateDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setDisplayName(Mockito.<String>any());
        doNothing().when(userProfile).setEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setHashPassword(Mockito.<String>any());
        doNothing().when(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        doNothing().when(userProfile).setLoginName(Mockito.<String>any());
        doNothing().when(userProfile).setNewEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        doNothing().when(userProfile).setResetPasswordCode(Mockito.<String>any());
        doNothing().when(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        doNothing().when(userProfile).setRoles(Mockito.<List<String>>any());
        doNothing().when(userProfile).setUserID(Mockito.<Long>any());
        doNothing().when(userProfile).setVerificationCode(Mockito.<String>any());
        doNothing().when(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
        userProfile.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        userProfile.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setDisplayName("Display Name");
        userProfile.setEmailAddress("42 Main St");
        userProfile.setHashPassword("iloveyou");
        userProfile.setIsEmailAddressVerified(true);
        userProfile.setIsEnable(true);
        userProfile.setLoginName("Login Name");
        userProfile.setNewEmailAddress("42 Main St");
        userProfile.setRefreshTokens(new ArrayList<>());
        userProfile.setResetPasswordCode("Reset Password Code");
        userProfile
                .setResetPasswordExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setRoles(new ArrayList<>());
        userProfile.setUpdateBy("2020-03-01");
        userProfile.setUpdateDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setUserID(1L);
        userProfile.setVerificationCode("Verification Code");
        userProfile
                .setVerificationExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Optional<UserProfile> ofResult = Optional.of(userProfile);
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(ofResult);
        userProfileServiceImpl.resetPassword("42 Main St", null);
    }

    /**
     * Method under test: {@link UserProfileServiceImpl#resetPassword(String, ResetPasswordDTO)}
     */
    @Test
    void testResetPassword7() {
        UserProfile userProfile = mock(UserProfile.class);
        when(userProfile.getResetPasswordExpiredCodeTime())
                .thenThrow(new BadCredentialsException("Start reset password!"));
        when(userProfile.getResetPasswordCode()).thenReturn("Code");
        doNothing().when(userProfile).setCreatedBy(Mockito.<String>any());
        doNothing().when(userProfile).setCreatedDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setIsEnable(Mockito.<Boolean>any());
        doNothing().when(userProfile).setUpdateBy(Mockito.<String>any());
        doNothing().when(userProfile).setUpdateDate(Mockito.<Instant>any());
        doNothing().when(userProfile).setDisplayName(Mockito.<String>any());
        doNothing().when(userProfile).setEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setHashPassword(Mockito.<String>any());
        doNothing().when(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        doNothing().when(userProfile).setLoginName(Mockito.<String>any());
        doNothing().when(userProfile).setNewEmailAddress(Mockito.<String>any());
        doNothing().when(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        doNothing().when(userProfile).setResetPasswordCode(Mockito.<String>any());
        doNothing().when(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        doNothing().when(userProfile).setRoles(Mockito.<List<String>>any());
        doNothing().when(userProfile).setUserID(Mockito.<Long>any());
        doNothing().when(userProfile).setVerificationCode(Mockito.<String>any());
        doNothing().when(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
        userProfile.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        userProfile.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setDisplayName("Display Name");
        userProfile.setEmailAddress("42 Main St");
        userProfile.setHashPassword("iloveyou");
        userProfile.setIsEmailAddressVerified(true);
        userProfile.setIsEnable(true);
        userProfile.setLoginName("Login Name");
        userProfile.setNewEmailAddress("42 Main St");
        userProfile.setRefreshTokens(new ArrayList<>());
        userProfile.setResetPasswordCode("Reset Password Code");
        userProfile
                .setResetPasswordExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setRoles(new ArrayList<>());
        userProfile.setUpdateBy("2020-03-01");
        userProfile.setUpdateDate(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        userProfile.setUserID(1L);
        userProfile.setVerificationCode("Verification Code");
        userProfile
                .setVerificationExpiredCodeTime(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        Optional<UserProfile> ofResult = Optional.of(userProfile);
        when(userProfileRepository.findOneByEmailAddressVerified(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(BadCredentialsException.class,
                () -> userProfileServiceImpl.resetPassword("42 Main St", new ResetPasswordDTO("iloveyou", "Code")));
        verify(userProfileRepository).findOneByEmailAddressVerified(Mockito.<String>any());
        verify(userProfile).getResetPasswordCode();
        verify(userProfile).getResetPasswordExpiredCodeTime();
        verify(userProfile).setCreatedBy(Mockito.<String>any());
        verify(userProfile).setCreatedDate(Mockito.<Instant>any());
        verify(userProfile).setIsEnable(Mockito.<Boolean>any());
        verify(userProfile).setUpdateBy(Mockito.<String>any());
        verify(userProfile).setUpdateDate(Mockito.<Instant>any());
        verify(userProfile).setDisplayName(Mockito.<String>any());
        verify(userProfile).setEmailAddress(Mockito.<String>any());
        verify(userProfile).setHashPassword(Mockito.<String>any());
        verify(userProfile).setIsEmailAddressVerified(Mockito.<Boolean>any());
        verify(userProfile).setLoginName(Mockito.<String>any());
        verify(userProfile).setNewEmailAddress(Mockito.<String>any());
        verify(userProfile).setRefreshTokens(Mockito.<List<RefreshToken>>any());
        verify(userProfile).setResetPasswordCode(Mockito.<String>any());
        verify(userProfile).setResetPasswordExpiredCodeTime(Mockito.<Instant>any());
        verify(userProfile).setRoles(Mockito.<List<String>>any());
        verify(userProfile).setUserID(Mockito.<Long>any());
        verify(userProfile).setVerificationCode(Mockito.<String>any());
        verify(userProfile).setVerificationExpiredCodeTime(Mockito.<Instant>any());
    }

}

