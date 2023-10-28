package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateResetPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidateResetPassword
public class ResetPasswordDTO {
    private String password;
    private String code;
}
