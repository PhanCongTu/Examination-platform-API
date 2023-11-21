package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.SubmitMCTestDTO;
import com.example.springboot.dto.response.ScoreResponse;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.Score;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.MultipleChoiceTestRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.repository.ScoreRepository;
import com.example.springboot.repository.TestQuestionRepository;
import com.example.springboot.service.ScoreService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final WebUtils webUtils;
    private final ScoreRepository scoreRepository;
    private final MultipleChoiceTestRepository multipleChoiceTestRepository;
    private final QuestionRepository questionRepository;
    private final TestQuestionRepository testQuestionRepository;
    @Override
    public ResponseEntity<?> submitTest(SubmitMCTestDTO dto) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        Optional<MultipleChoiceTest> multipleChoiceTestOp =
                multipleChoiceTestRepository.findById(dto.getMultipleChoiceTestId());
        if(multipleChoiceTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }

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
        if(multipleChoiceTestOp.get().getEndDate() < unixTimeNow) {
            isLate = true;
        }

        AtomicReference<Double> totalScore = new AtomicReference<>(0.00);
        Long totalQuestion = testQuestionRepository.countAllByMultipleChoiceTestId(dto.getMultipleChoiceTestId());
        Double eachQuestionScore =  (10/(double)totalQuestion);
        dto.getSubmittedAnswers().forEach((item -> {
            Optional<Question> question = questionRepository.findById(item.getQuestionId());
            if (question.isPresent()) {
                if(question.get().getCorrectAnswer().equals(item.getAnswer())) {
                    totalScore.updateAndGet(v -> v + eachQuestionScore);
                }
            }
        }));

        Score score = Score.builder()
                .isLate(isLate)
                .totalCore(totalScore.getOpaque())
                .totalCore((int)(Math.round(totalScore.getOpaque() * 100))/100.0)
                .multipleChoiceTest(multipleChoiceTestOp.get())
                .userProfile(userProfile)
                .build();
        score = scoreRepository.save(score);
        ScoreResponse response = CustomBuilder.buildScoreResponse(score);
        return ResponseEntity.ok(response);
    }

    private void modifyUpdateScore(Question question) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        question.setUpdateBy(userProfile.getLoginName());
        question.setUpdateDate(Instant.now());
    }
}
