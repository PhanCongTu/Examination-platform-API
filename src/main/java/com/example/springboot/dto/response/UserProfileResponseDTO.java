package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Builder
public class UserProfileResponseDTO {
    private Long userID;
    private String loginName;
    private String displayName;
    private String emailAddress;
    private String newEmailAddress;
    private Boolean isEmailAddressVerified;
    private List<String> roles;
}
