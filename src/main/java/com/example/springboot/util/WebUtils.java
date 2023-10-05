package com.example.springboot.util;

import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.UserProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WebUtils {
    private UserProfileRepository userProfileRepository;
    public UserProfile getCurrentLogedInUser() {
        // Get current logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserProfile userProfile = userProfileRepository.findOneByLoginName(auth.getName()).orElseThrow(
                UserNotFoundException::new
        );
        return userProfile;
    }
}
