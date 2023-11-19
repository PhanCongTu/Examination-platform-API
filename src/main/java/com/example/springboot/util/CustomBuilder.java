package com.example.springboot.util;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.response.ClassroomResponse;
import com.example.springboot.dto.response.MultipleChoiceTestResponse;
import com.example.springboot.dto.response.MultipleChoiceTestWithQuestionsResponse;
import com.example.springboot.dto.response.QuestionGroupResponse;
import com.example.springboot.dto.response.QuestionResponse;
import com.example.springboot.dto.response.UserProfileResponse;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.entity.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

public class CustomBuilder {

    public static MultipleChoiceTestResponse buildMultipleChoiceTest(MultipleChoiceTest multipleChoiceTest) {
        return MultipleChoiceTestResponse.builder()
                .id(multipleChoiceTest.getId())
                .testName(multipleChoiceTest.getTestName())
                .startDate(multipleChoiceTest.getStartDate())
                .endDate(multipleChoiceTest.getEndDate())
                .testingTime(multipleChoiceTest.getTestingTime())
                .build();
    }

    public static MultipleChoiceTestWithQuestionsResponse buildMultipleChoiceTestWithQuestions(MultipleChoiceTest multipleChoiceTest, List<QuestionResponse> questions) {
        return MultipleChoiceTestWithQuestionsResponse.builder()
                .id(multipleChoiceTest.getId())
                .testName(multipleChoiceTest.getTestName())
                .startDate(multipleChoiceTest.getStartDate())
                .endDate(multipleChoiceTest.getEndDate())
                .testingTime(multipleChoiceTest.getTestingTime())
                .questions(questions)
                .build();
    }

    public static ClassroomResponse buildClassroomResponse(Classroom classRoom){
        return ClassroomResponse.builder()
                .id(classRoom.getId())
                .className(classRoom.getClassName())
                .classCode(classRoom.getClassCode())
                .isPrivate(classRoom.getIsPrivate())
                .isEnable(classRoom.getIsEnable())
                .build();
    }

    public static UserProfileResponse buildUserProfileResponse(UserProfile userProfile){
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

    public static QuestionGroupResponse buildQuestionGroupResponse(QuestionGroup questionGroup){
        return QuestionGroupResponse.builder()
                .id(questionGroup.getId())
                .name(questionGroup.getName())
                .code(questionGroup.getCode())
                .isEnable(questionGroup.getIsEnable())
                .build();
    }

    public static QuestionResponse buildQuestionResponse(Question question){
        return QuestionResponse.builder()
                .id(question.getId())
                .content(question.getContent())
                .firstAnswer(question.getFirstAnswer())
                .secondAnswer(question.getSecondAnswer())
                .thirdAnswer(question.getThirdAnswer())
                .fourthAnswer(question.getFourthAnswer())
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
    public static ResponseEntity<LinkedHashMap<String, String>> buildQuestionGroupNotFoundResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.QUESTION_GROUP_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.QUESTION_GROUP_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    public static ResponseEntity<LinkedHashMap<String, String>> buildQuestionNotFoundResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.QUESTION_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.QUESTION_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    public static ResponseEntity<LinkedHashMap<String, String>> buildMultipleChoiceTestNotFoundResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.MULTIPLE_CHOICE_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.MULTIPLE_CHOICE_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    public static ResponseEntity<LinkedHashMap<String, String>> buildMultipleChoiceTestTestDateInvalidResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_DATE_INVALID.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_DATE_INVALID.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
