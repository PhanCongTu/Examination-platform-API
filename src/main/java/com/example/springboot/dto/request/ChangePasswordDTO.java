package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateChangePassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidateChangePassword
@Builder
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
