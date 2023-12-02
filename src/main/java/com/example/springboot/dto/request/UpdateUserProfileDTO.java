package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateUpdateUserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateUpdateUserProfile
public class UpdateUserProfileDTO {
    private String displayName;
    private String emailAddress;
}
