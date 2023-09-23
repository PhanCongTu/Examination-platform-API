package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateChangePasswordRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidateChangePasswordRequest
@Builder
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
