package com.example.springboot.util;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.response.QuestionGroupResponse;
import com.example.springboot.dto.response.UserProfileResponse;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.entity.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;

public class CustomBuilder {

    public static UserProfileResponse builtUserProfileResponse(UserProfile userProfile){
        return UserProfileResponse.builder()
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

    public static QuestionGroupResponse builtQuestionGroupResponse(QuestionGroup questionGroup){
        return QuestionGroupResponse.builder()
                .id(questionGroup.getId())
                .name(questionGroup.getName())
                .code(questionGroup.getCode())
                .isEnable(questionGroup.getIsEnable())
                .build();
    }
    /**
     * Build an error response when the Classroom is not found
     *
     * @return the response
     */
    public static ResponseEntity<LinkedHashMap<String, String>> buildClassroomNotFoundResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.CLASSROOM_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.CLASSROOM_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
