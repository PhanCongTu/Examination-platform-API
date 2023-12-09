package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.SubmitMCTestDTO;
import com.example.springboot.dto.response.MyScoreResponse;
import com.example.springboot.dto.response.ScoreResponse;
import com.example.springboot.dto.response.StudentScoreResponse;
import com.example.springboot.dto.response.SubmittedQuestionResponse;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.Score;
import com.example.springboot.entity.SubmittedQuestion;
import com.example.springboot.entity.TestTracking;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.MultipleChoiceTestRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.repository.ScoreRepository;
import com.example.springboot.repository.SubmittedQuestionRepository;
import com.example.springboot.repository.TestQuestionRepository;
import com.example.springboot.repository.TestTrackingRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.ScoreService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final SubmittedQuestionRepository submittedQuestionRepository;

    private final WebUtils webUtils;
    private final ScoreRepository scoreRepository;
    private final MultipleChoiceTestRepository multipleChoiceTestRepository;
    private final QuestionRepository questionRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final UserProfileRepository userProfileRepository;
    private final TestTrackingRepository testTrackingRepository;

    @Override
    public ResponseEntity<?> getScoreOfStudent(Long studentId, Long multipleChoiceTestId) {
        Optional<UserProfile> studentOp = userProfileRepository.findById(studentId);
        if (studentOp.isEmpty()) {
            return CustomBuilder.buildStudentNotFoundResponseEntity();
        }
        Optional<Score> score = scoreRepository.findByMultipleChoiceTestIdAndUserProfileUserID(multipleChoiceTestId, studentId);
        if (score.isEmpty()){
            return CustomBuilder.buildScoreNotFoundResponseEntity();
        }

        List<SubmittedQuestion> submittedQuestions = submittedQuestionRepository.findAllByScoreId(score.get().getId());
        List<SubmittedQuestionResponse> submittedQuestionResponses
                = submittedQuestions.stream()
                .map(CustomBuilder::buildSubmittedQuestionResponse)
                .collect(Collectors.toList());
        ScoreResponse response = CustomBuilder.buildScoreResponse(score.get(), submittedQuestionResponses);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getAllStudentScoreOfTest(Long testId, String search, int page, String column, int size, String sortType) {
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Optional<MultipleChoiceTest> MCTestOp = multipleChoiceTestRepository.findById(testId);
        if (MCTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        String searchText = "%" + search.trim() + "%";
        Page<StudentScoreResponse> scores =  scoreRepository.findAllScoreOfMultipleChoiceTest(testId, searchText, pageable);
        return ResponseEntity.ok(scores);
    }

    @Override
    public ResponseEntity<?> submitTest(SubmitMCTestDTO dto) {
        // Current logged-in student
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        // Get the test which student submitted
        Optional<MultipleChoiceTest> multipleChoiceTestOp =
                multipleChoiceTestRepository.findById(dto.getMultipleChoiceTestId());
        if(multipleChoiceTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        // Response error if this test has been submitted
        Optional<Score> scoreOp =
                scoreRepository
                        .findByMultipleChoiceTestIdAndUserProfileUserID(dto.getMultipleChoiceTestId(), userProfile.getUserID());
        if(scoreOp.isPresent()) {
            // The test is not started
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.SCORE_TEST_SUBMITTED.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.SCORE_TEST_SUBMITTED.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        // Check the time when student submit the test
        boolean isLate = false;
        Long unixTimeNow = Timestamp.from(ZonedDateTime.now().toInstant()).getTime();
        if(multipleChoiceTestOp.get().getStartDate() > unixTimeNow) {
            // The test is not started
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_SUBMIT_NOT_STARTED_TEST.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_SUBMIT_NOT_STARTED_TEST.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        // Submit late
        if(multipleChoiceTestOp.get().getEndDate() < unixTimeNow) {
            isLate = true;
        }

        Double totalScore = 0.00;
        Long totalCorrect = 0L;
        Long totalQuestion = testQuestionRepository.countAllByMultipleChoiceTestId(dto.getMultipleChoiceTestId());
        Double eachQuestionScore =  (10/(double)totalQuestion);

        Score score = Score.builder()
                .isLate(isLate)
                .multipleChoiceTest(multipleChoiceTestOp.get())
                .userProfile(userProfile)
                .submittedDate(Timestamp.from(ZonedDateTime.now().toInstant()).getTime())
                .build();
        score = scoreRepository.save(score);
        List<SubmittedQuestionResponse> submittedQuestionResponses = new ArrayList<>();
        for (SubmitMCTestDTO.SubmittedAnswer item : dto.getSubmittedAnswers()) {
            Optional<Question> questionOp = questionRepository.findById(item.getQuestionId());
            if (questionOp.isPresent()) {
                Question question = questionOp.get();
                SubmittedQuestion submittedQuestion = SubmittedQuestion.builder()
                        .questionId(question.getId())
                        .content(question.getContent())
                        .firstAnswer(question.getFirstAnswer())
                        .secondAnswer(question.getSecondAnswer())
                        .thirdAnswer(question.getThirdAnswer())
                        .fourthAnswer(question.getFourthAnswer())
                        .correctAnswer(question.getCorrectAnswer())
                        .submittedAnswer(item.getAnswer())
                        .score(score)
                        .build();
                submittedQuestion = submittedQuestionRepository.save(submittedQuestion);
                submittedQuestionResponses.add(CustomBuilder.buildSubmittedQuestionResponse(submittedQuestion));
                if(question.getCorrectAnswer().equals(item.getAnswer())) {
                    totalScore += eachQuestionScore;
                    totalCorrect += 1;
                }
            }
        }
        score.setTotalCore((int)(Math.round(totalScore * 100))/100.0);
        score.setTotalCorrect(totalCorrect);
        score = scoreRepository.save(score);

        // Delete test tracking
        Optional<TestTracking> testTracking = testTrackingRepository.findByMultipleChoiceTestIdAndUserProfileUserID(
                multipleChoiceTestOp.get().getId(), userProfile.getUserID());
        testTrackingRepository.delete(testTracking.get());

        ScoreResponse response = CustomBuilder.buildScoreResponse(score, submittedQuestionResponses);
        return ResponseEntity.ok(response);
    }

    private void modifyUpdateScore(Question question) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        question.setUpdateBy(userProfile.getLoginName());
        question.setUpdateDate(Instant.now());
    }

    @Override
    public ResponseEntity<?> getAllScoreOfStudent(Long userID,String search, Long dateFrom, Long dateTo, int page, String column, int size, String sortType) {
        Optional<UserProfile> studentOp = userProfileRepository.findById(userID);
        if (studentOp.isEmpty()) {
            return CustomBuilder.buildStudentNotFoundResponseEntity();
        }
        String searchText = "%" + search.trim() + "%";
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Page<MyScoreResponse> response = scoreRepository.findAllMyScores(studentOp.get().getUserID(),searchText, dateFrom, dateTo, pageable);
        return ResponseEntity.ok(response);
    }
}
