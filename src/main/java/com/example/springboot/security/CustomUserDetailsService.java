package com.example.springboot.security;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    public CustomUserDetailsService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = userProfileRepository.findOneByLoginName(username).orElse(null);
        if (user==null){
            log.error(String.format(ErrorMessage.LOGIN_NAME_NOT_FOUND.getMessage(), username));
            throw new UsernameNotFoundException(username);
        }
        return getUserDetails(user);
    }

    private JwtUserDetails getUserDetails(UserProfile user) {
        return new JwtUserDetails(
                user.getDisplayName(),
                user.getLoginName(),
                user.getHashPassword(),
                user.getEmailAddress(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                user.getIsEnable()
        );
    }
}
