package com.example.springboot;

import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.util.EnumRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private UserProfileRepository userProfileRepository;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userProfileRepository.count() == 0){
            UserProfile userProfile1 = new UserProfile(
                    "admin",
                    "123",
                    "ROLE ADMIN",
                    "admin@email.com",
                    Collections.singletonList(EnumRole.ROLE_ADMIN.name())
            );
            UserProfile userProfile2 = new UserProfile(
                    "user",
                    "123",
                    "ROLE USER",
                    "user@email.com",
                    Collections.singletonList(EnumRole.ROLE_USER.name())
            );
            userProfileRepository.save(userProfile1);
            userProfileRepository.save(userProfile2);
        }
    }
}
