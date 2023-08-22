package com.example.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

    private String displayName;

    private String token;

    private String avatar;

    private long expired;

    private List<String> roles = new ArrayList<>();
}
