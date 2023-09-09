package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateVerificationEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidateVerificationEmail
public class VerificationEmailDTO {
    private String code;
}
