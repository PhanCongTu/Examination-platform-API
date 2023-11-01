package com.example.springboot.util;

import com.example.springboot.dto.response.UserProfileResponseDTO;
import com.example.springboot.entity.UserProfile;

public class Mapper {

    public static UserProfileResponseDTO mapUserProfileToDTO(UserProfile userProfile){
        return UserProfileResponseDTO.builder()
                .userID(userProfile.getUserID())
                .displayName(userProfile.getDisplayName())
                .emailAddress(userProfile.getEmailAddress())
                .roles(userProfile.getRoles())
                .newEmailAddress(userProfile.getNewEmailAddress())
                .isEnable(userProfile.getIsEnable())
                .isEmailAddressVerified(userProfile.getIsEmailAddressVerified())
                .loginName(userProfile.getLoginName())
                .build();
    }
}
