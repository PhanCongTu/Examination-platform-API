package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateResetPasswordRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidateResetPasswordRequest
public class ResetPasswordDTO {
    private String password;
    private String code;
}
