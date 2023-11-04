package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidateLogin
@Builder
public class LoginDTO {

    private String loginName;

    private String password;
}
