package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateLoginRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidateLoginRequest
@Builder
public class LoginRequestDTO {

    private String loginName;

    private String password;
}
