package com.example.springboot.util;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.response.ClassroomResponse;
import com.example.springboot.dto.response.MultipleChoiceTestResponse;
import com.example.springboot.dto.response.MultipleChoiceTestWithQuestionsResponse;
import com.example.springboot.dto.response.QuestionGroupResponse;
import com.example.springboot.dto.response.QuestionResponse;
import com.example.springboot.dto.response.ScoreResponse;
import com.example.springboot.dto.response.SubmittedQuestionResponse;
import com.example.springboot.dto.response.TestTrackingResponse;
import com.example.springboot.dto.response.UserProfileResponse;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.entity.Score;
import com.example.springboot.entity.SubmittedQuestion;
import com.example.springboot.entity.TestTracking;
import com.example.springboot.entity.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;

public class CustomBuilder {

    public static ScoreResponse buildScoreResponse(Score score, List<SubmittedQuestionResponse> submittedQuestionResponses) {
        return ScoreResponse.builder()
                .id(score.getId())
                .totalScore(score.getTotalCore())
                .isLate(score.isLate())
                .SubmittedDate(score.getCreatedDate().toEpochMilli())
                .multipleChoiceTest(CustomBuilder.buildMultipleChoiceTestResponse(score.getMultipleChoiceTest()))
                .submittedQuestions(submittedQuestionResponses)
                .targetScore(score.getTargetScore())
                .build();
    }

    public static SubmittedQuestionResponse buildSubmittedQuestionResponse(SubmittedQuestion value) {
        return SubmittedQuestionResponse.builder()
                .id(value.getId())
                .questionId(value.getQuestionId())
                .content(value.getContent())
                .firstAnswer(value.getFirstAnswer())
                .secondAnswer(value.getSecondAnswer())
                .thirdAnswer(value.getThirdAnswer())
                .fourthAnswer(value.getFourthAnswer())
                .correctAnswer(value.getCorrectAnswer())
                .submittedAnswer(value.getSubmittedAnswer())
                .build();
    }

    public static MultipleChoiceTestResponse buildMultipleChoiceTestResponse(MultipleChoiceTest multipleChoiceTest) {
        return MultipleChoiceTestResponse.builder()
                .id(multipleChoiceTest.getId())
                .testName(multipleChoiceTest.getTestName())
                .description(multipleChoiceTest.getDescription())
                .startDate(multipleChoiceTest.getStartDate())
                .endDate(multipleChoiceTest.getEndDate())
                .testingTime(multipleChoiceTest.getTestingTime())
                .targetScore(multipleChoiceTest.getTargetScore())
                .build();
    }

    public static MultipleChoiceTestWithQuestionsResponse buildMultipleChoiceTestWithQuestionsResponse(MultipleChoiceTest multipleChoiceTest, List<QuestionResponse> questions) {
        return MultipleChoiceTestWithQuestionsResponse.builder()
                .id(multipleChoiceTest.getId())
                .testName(multipleChoiceTest.getTestName())
                .description(multipleChoiceTest.getDescription())
                .startDate(multipleChoiceTest.getStartDate())
                .endDate(multipleChoiceTest.getEndDate())
                .testingTime(multipleChoiceTest.getTestingTime())
                .questions(questions)
                .targetScore(multipleChoiceTest.getTargetScore())
                .build();
    }

    public static ClassroomResponse buildClassroomResponse(Classroom classRoom){
        return ClassroomResponse.builder()
                .id(classRoom.getId())
                .className(classRoom.getClassName())
                .classCode(classRoom.getClassCode())
                .description(classRoom.getDescription())
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
                .description(questionGroup.getDescription())
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

    public static ResponseEntity<?> buildStudentNotFoundResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.STUDENT_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.STUDENT_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    public static ResponseEntity<?> buildScoreNotFoundResponseEntity() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.SCORE_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.SCORE_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    public static TestTrackingResponse buildTestTrackingResponse(TestTracking testTracking) {
        return TestTrackingResponse.builder()
                .id(testTracking.getId())
                .firstTimeAccess(testTracking.getFirstTimeAccess())
                .dueTime(testTracking.getDueTime())
                .multipleChoiceTestId(testTracking.getMultipleChoiceTest().getId())
                .studentId(testTracking.getUserProfile().getUserID())
                .build();
    }
}
