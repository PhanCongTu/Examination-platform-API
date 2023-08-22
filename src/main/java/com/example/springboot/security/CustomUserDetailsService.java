package com.example.springboot.security;

import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile user = userRepository.findOneByLoginName(username).orElse(null);
        if (user==null){
            log.error("Can not find user with login name {}", username);
            throw new UsernameNotFoundException(username);
        }
        return getUserDetails(user);
    }

    private JwtUserDetails getUserDetails(UserProfile user) {
        return new JwtUserDetails(
                user.getDisplayName(),
                user.getLoginName(),
                user.getHashPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                user.getIsDisable()
        );
    }
}
