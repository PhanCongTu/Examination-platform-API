package com.example.springboot.dto.view_model;

import com.example.springboot.validate.ValidateSignupVM;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@ValidateSignupVM
public class SignupVM {

    private String loginName;

    private String password;

    private String displayName;

    private String emailAddress;
}
