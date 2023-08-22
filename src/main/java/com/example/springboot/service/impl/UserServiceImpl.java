package com.example.springboot.service.impl;

import com.example.springboot.dto.TokenDetails;
import com.example.springboot.dto.response.LoginResponseDTO;
import com.example.springboot.dto.response.SignupResponseDTO;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.service.AuthService;
import com.example.springboot.service.UserService;
import com.example.springboot.util.EnumRole;
import com.example.springboot.dto.view_model.LoginVM;
import com.example.springboot.dto.view_model.SignupVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthService authService;

    @Override
    @Transactional
    public ResponseEntity<?> createUser(SignupVM signupVM) {
        log.info("Start createUser()");
        UserProfile newUserProfile = new UserProfile();
        newUserProfile.setDisplayName(signupVM.getDisplayName());
        newUserProfile.setEmailAddress(signupVM.getEmailAddress());
        newUserProfile.setHashPassword(signupVM.getPassword());
        newUserProfile.setLoginName(signupVM.getLoginName());
        newUserProfile.setEmailAddressVerified(false);
        newUserProfile.setIsDisable(false);
        newUserProfile.setRoles(Arrays.asList(EnumRole.ROLE_USER.name()));
        userRepository.save(newUserProfile);

        TokenDetails tokenDetails = authService.authenticate(
                new LoginVM(signupVM.getLoginName(), signupVM.getPassword(), null, null)
        );
        log.info("End createUser()");
        return ResponseEntity.ok(new SignupResponseDTO(
                signupVM.getDisplayName(),
                signupVM.getLoginName(),
                signupVM.getEmailAddress(),
                tokenDetails.getToken(), null));
    }

    @Override
    public ResponseEntity<?> login(LoginVM loginVM) {
        TokenDetails tokenDetails = authService.authenticate(loginVM);
        return ResponseEntity.ok(new LoginResponseDTO(
                tokenDetails.getToken(),
                null,
                tokenDetails.getExpired()
        ));
    }
}
