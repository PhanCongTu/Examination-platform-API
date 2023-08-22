package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO {

    private String displayName;

    private String loginName;

    private String emailAddress;

    private String token;

    private String refreshToken;

}
