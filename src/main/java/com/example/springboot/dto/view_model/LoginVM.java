package com.example.springboot.dto.view_model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginVM {
    private String loginName;
    private String password;
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
