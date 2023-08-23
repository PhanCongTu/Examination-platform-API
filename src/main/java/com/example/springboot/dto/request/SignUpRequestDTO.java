package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateSignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidateSignUpRequest
public class SignUpRequestDTO {

    private String loginName;

    private String password;

    private String displayName;

    private String emailAddress;
}
