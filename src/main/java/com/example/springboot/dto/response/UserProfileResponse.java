package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class UserProfileResponse {
    private Long userID;
    private String loginName;
    private String displayName;
    private String emailAddress;
    private String newEmailAddress;
    private Boolean isEmailAddressVerified;
    private Boolean isEnable;
    private List<String> roles;
}
