package com.example.springboot;

import com.example.springboot.dto.request.SignUpRequestDTO;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserProfileRepository userProfileRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userProfileRepository.count() == 0) {
            SignUpRequestDTO value = SignUpRequestDTO.builder()
                    .loginName("admin")
                    .password("admin")
                    .displayName("ADMIN")
                    .emailAddress("admin@gmail.com")
                    .displayName("ADMIN")
                    .build();
            Boolean isTeacher = false;
            Boolean isAdmin = true;
            userProfileService.createUser(value, isTeacher, isAdmin);
        }
    }
}
