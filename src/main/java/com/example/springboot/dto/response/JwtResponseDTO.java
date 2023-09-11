package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO {
    private String displayName;

    private String loginName;

    private String emailAddress;

    private Boolean isEmailAddressVerified;

    private String accessToken;

    private String refreshToken;

    private List<String> roles;

    private Long expired_in;

}
