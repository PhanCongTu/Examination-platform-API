package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateMultipleChoiceTestDTO;
import com.example.springboot.dto.request.UpdateMultipleChoiceTestDTO;
import com.example.springboot.dto.response.MultipleChoiceTestResponse;
import com.example.springboot.dto.response.MultipleChoiceTestWithQuestionsResponse;
import com.example.springboot.dto.response.MyMultipleChoiceTestResponse;
import com.example.springboot.dto.response.QuestionResponse;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.Score;
import com.example.springboot.entity.TestQuestion;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.QuestionNotFoundException;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.MultipleChoiceTestRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.repository.ScoreRepository;
import com.example.springboot.repository.TestQuestionRepository;
import com.example.springboot.service.MailService;
import com.example.springboot.service.MultipleChoiceTestService;
import com.example.springboot.service.QuestionService;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MultipleChoiceTestServiceImpl implements MultipleChoiceTestService {
    private final TestQuestionRepository testQuestionRepository;

    private final WebUtils webUtils;
    private final MultipleChoiceTestRepository multipleChoiceTestRepository;
    private final ClassroomRepository classroomRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final MailService mailService;
    private final ScoreRepository scoreRepository;

    @Override
    public ResponseEntity<?> getMultipleChoiceTest(Long testId) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        Optional<MultipleChoiceTest> multipleChoiceTestOp = multipleChoiceTestRepository.findById(testId);
        if(multipleChoiceTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        MultipleChoiceTest multipleChoiceTest = multipleChoiceTestOp.get();
        List<Long> questionIds = testQuestionRepository.findQuestionIdsOfTest(multipleChoiceTest.getId());
        List<Question> questions = questionRepository.findAllByIds(questionIds);
        List<QuestionResponse> questionsOfTheTest =
                questions.stream()
                        .map(CustomBuilder::buildQuestionResponse)
                        .collect(Collectors.toList());
        MultipleChoiceTestWithQuestionsResponse response =
                CustomBuilder.buildMultipleChoiceTestWithQuestionsResponse(multipleChoiceTest, questionsOfTheTest);
        Optional<Score> score = scoreRepository.findByMultipleChoiceTestIdAndUserProfileUserID(response.getId(), myId);
        response.setIsSubmitted(score.isPresent());

        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getMyMultipleChoiceTestsNext2Weeks(String search, int page, String column, int size, String sortType) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
//        Long unixTime2WeeksAgo = Date.from(Instant.now()).getTime();
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search.trim() + "%";
        Long unixTime2WeeksAgo = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        Long unixTime2WeeksLater = Timestamp.from(ZonedDateTime.now().toInstant().plus(Period.ofWeeks(2))).getTime();
        List<MyMultipleChoiceTestResponse> response =
                multipleChoiceTestRepository.find2WeeksAroundMCTest(myId, unixTime2WeeksAgo, unixTime2WeeksLater,searchText,pageable);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getMyMultipleChoiceTestsOf2WeeksAround(String search, int page, String column, int size, String sortType) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search.trim() + "%";
        Long unixTime2WeeksAgo = Timestamp.from(ZonedDateTime.now().toInstant().minus(Period.ofWeeks(2))).getTime();
        Long unixTime2WeeksLater = Timestamp.from(ZonedDateTime.now().toInstant().plus(Period.ofWeeks(2))).getTime();
        List<MyMultipleChoiceTestResponse> response =
                multipleChoiceTestRepository.find2WeeksAroundMCTest(myId, unixTime2WeeksAgo, unixTime2WeeksLater,searchText,pageable);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<?> getMyMultipleChoiceTestsToday(Long startOfDate,Long endOfDate, String search, int page, String column, int size, String sortType) {

        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search.trim() + "%";
        Page<MyMultipleChoiceTestResponse> response ;
        if(Objects.nonNull(endOfDate)){
            response =
                    multipleChoiceTestRepository.findMCTestByDay(myId, startOfDate ,endOfDate,searchText,pageable);
        } else {
            response = multipleChoiceTestRepository.
                    findMyNotEndedMultipleChoiceTests(myId,startOfDate, searchText, pageable);
        }
        response.forEach((item)->{
            Optional<Score> score = scoreRepository.findByMultipleChoiceTestIdAndUserProfileUserID(item.getId(), myId);
            if (score.isPresent()) {
                item.setIsSubmitted(Boolean.TRUE);
            }
        });
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getInfoMultipleChoiceTest(Long testId) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        MyMultipleChoiceTestResponse response =
                multipleChoiceTestRepository.findMultipleChoiceTestInformation(testId, myId);
        if (Objects.isNull(response)) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getMyMultipleChoiceTests(boolean isEnded, String search, int page, String column, int size, String sortType) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search.trim() + "%";
        Long unixTimeNow = Timestamp.from(ZonedDateTime.now().toInstant()).getTime();
        Page<MyMultipleChoiceTestResponse> multipleChoiceTests;
        if (isEnded) {
            multipleChoiceTests = multipleChoiceTestRepository.
                    findMyEndedMultipleChoiceTests(myId,unixTimeNow, searchText, pageable);
        } else {
            multipleChoiceTests = multipleChoiceTestRepository.
                    findMyNotEndedMultipleChoiceTests(myId,unixTimeNow, searchText, pageable);
        }
        return ResponseEntity.ok(multipleChoiceTests);
    }

    @Override
    public ResponseEntity<?> getMultipleChoiceTestsOfClassroom(Long classroomId,boolean isEnded, String search, int page, String column, int size, String sortType) {
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Optional<Classroom> classRoom =  classroomRepository.findById(classroomId);
        if (classRoom.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        String searchText = "%" + search.trim() + "%";
        Long unixTimeNow = Timestamp.from(ZonedDateTime.now().toInstant()).getTime();
        Page<MultipleChoiceTest> multipleChoiceTests;
        if (isEnded) {
            multipleChoiceTests = multipleChoiceTestRepository.
                    findEndedMultipleChoiceTestOfClassroomByClassroomId(classroomId,unixTimeNow, searchText, pageable);
        } else {
            multipleChoiceTests = multipleChoiceTestRepository.
                    findNotEndedMultipleChoiceTestOfClassroomByClassroomId(classroomId,unixTimeNow, searchText, pageable);
        }
        Page<MultipleChoiceTestResponse> response = multipleChoiceTests.map(CustomBuilder::buildMultipleChoiceTestResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getMyMultipleChoiceTestsOfClassroom(Long classroomId, boolean isEnded, String search, int page, String column, int size, String sortType) {
        Long  myId = webUtils.getCurrentLogedInUser().getUserID();
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Optional<Classroom> classRoom =  classroomRepository.findById(classroomId);
        if (classRoom.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        String searchText = "%" + search.trim() + "%";
        Long unixTimeNow = Timestamp.from(ZonedDateTime.now().toInstant()).getTime();
        Page<MultipleChoiceTest> multipleChoiceTests;
        if (isEnded) {
            multipleChoiceTests = multipleChoiceTestRepository.
                    findEndedMultipleChoiceTestOfClassroomByClassroomId(classroomId,unixTimeNow, searchText, pageable);
        } else {
            multipleChoiceTests = multipleChoiceTestRepository.
                    findNotEndedMultipleChoiceTestOfClassroomByClassroomId(classroomId,unixTimeNow, searchText, pageable);
        }
        Page<MultipleChoiceTestResponse> response = multipleChoiceTests.map(CustomBuilder::buildMultipleChoiceTestResponse);

        response.forEach((item)->{
            Optional<Score> score = scoreRepository.findByMultipleChoiceTestIdAndUserProfileUserID(item.getId(), myId);
            item.setIsSubmitted(score.isPresent());
        });

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateMultipleChoiceTest(Long testId, UpdateMultipleChoiceTestDTO dto) {
        Optional<MultipleChoiceTest> multipleChoiceTestOp = multipleChoiceTestRepository.findById(testId);
        if(multipleChoiceTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        MultipleChoiceTest multipleChoiceTest = multipleChoiceTestOp.get();

        Long unixTimeNow = Timestamp.from(ZonedDateTime.now().toInstant()).getTime();
        if(multipleChoiceTest.getStartDate() < unixTimeNow) {
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_UPDATE_STARTED_TEST.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_UPDATE_STARTED_TEST.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        if (Objects.nonNull(dto.getTestName())) {
            multipleChoiceTest.setTestName(dto.getTestName());
            modifyUpdateMultipleChoiceTest(multipleChoiceTest);
        }
        if (Objects.nonNull(dto.getTestingTime())) {
            multipleChoiceTest.setTestingTime(dto.getTestingTime());
            modifyUpdateMultipleChoiceTest(multipleChoiceTest);
        }
        if (Objects.nonNull(dto.getTargetScore())) {
            multipleChoiceTest.setTargetScore(dto.getTargetScore());
            modifyUpdateMultipleChoiceTest(multipleChoiceTest);
        }
        if (Objects.nonNull(dto.getStartDate()) && Objects.isNull(dto.getEndDate())) {
            if (dto.getStartDate() < multipleChoiceTest.getEndDate()){
                multipleChoiceTest.setStartDate(dto.getStartDate());
                modifyUpdateMultipleChoiceTest(multipleChoiceTest);
            } else {
                return CustomBuilder.buildMultipleChoiceTestTestDateInvalidResponseEntity();
            }
        }
        if (Objects.nonNull(dto.getEndDate()) && Objects.isNull(dto.getStartDate())) {
            if (dto.getEndDate() > multipleChoiceTest.getStartDate()){
                multipleChoiceTest.setEndDate(dto.getEndDate());
                modifyUpdateMultipleChoiceTest(multipleChoiceTest);
            } else {
                return CustomBuilder.buildMultipleChoiceTestTestDateInvalidResponseEntity();
            }
        }
        // Validated in validator
        if (Objects.nonNull(dto.getEndDate()) && Objects.nonNull(dto.getStartDate())) {
            multipleChoiceTest.setEndDate(dto.getEndDate());
            multipleChoiceTest.setStartDate(dto.getStartDate());
            modifyUpdateMultipleChoiceTest(multipleChoiceTest);
        }
        multipleChoiceTest = multipleChoiceTestRepository.save(multipleChoiceTest);
        MultipleChoiceTestResponse response = CustomBuilder.buildMultipleChoiceTestResponse(multipleChoiceTest);
        mailService.sendTestUpdatedNotificationEmail(multipleChoiceTest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> deleteMultipleChoiceTest(Long testId) {
        Optional<MultipleChoiceTest> multipleChoiceTestOp = multipleChoiceTestRepository.findById(testId);
        if(multipleChoiceTestOp.isEmpty()) {
            return CustomBuilder.buildMultipleChoiceTestNotFoundResponseEntity();
        }
        MultipleChoiceTest multipleChoiceTest = multipleChoiceTestOp.get();
        Long unixTimeNow = Timestamp.from(ZonedDateTime.now().toInstant()).getTime();
        if(multipleChoiceTest.getStartDate() < unixTimeNow) {
            LinkedHashMap<String, String> response = new LinkedHashMap<>();
            response.put(Constants.ERROR_CODE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_DELETE_STARTED_TEST.getErrorCode());
            response.put(Constants.MESSAGE_KEY, ErrorMessage.MULTIPLE_CHOICE_TEST_DELETE_STARTED_TEST.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
        mailService.sendTestDeletedNotificationEmail(multipleChoiceTest);
        multipleChoiceTestRepository.deleteById(testId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<?> createMultipleChoiceTest(CreateMultipleChoiceTestDTO dto) throws QuestionNotFoundException {
        Classroom classroom = classroomRepository.findById(dto.getClassroomId()).get();
        MultipleChoiceTest multipleChoiceTest = new MultipleChoiceTest();
        multipleChoiceTest.setTestName(dto.getTestName());
        multipleChoiceTest.setDescription(dto.getDescription());
        multipleChoiceTest.setStartDate(dto.getStartDate());
        multipleChoiceTest.setEndDate(dto.getEndDate());
        multipleChoiceTest.setTestingTime(dto.getTestingTime());
        multipleChoiceTest.setClassRoom(classroom);
        multipleChoiceTest.setTargetScore(dto.getTargetScore());
        modifyUpdateMultipleChoiceTest(multipleChoiceTest);
        multipleChoiceTest = multipleChoiceTestRepository.save(multipleChoiceTest);
        List<QuestionResponse> questionsOfTheTest = new ArrayList<>();
        if(Objects.nonNull(dto.getQuestionIds())) {
            questionsOfTheTest =
                    addQuestionsToTestByQuestionId(multipleChoiceTest.getId(),dto.getQuestionIds());
        }
        if(Objects.nonNull(dto.getRandomQuestions())) {
            questionsOfTheTest =
                    addRandomQuestionToTestByQuestionGroupId
                            (multipleChoiceTest.getId(), dto.getRandomQuestions());
        }
        MultipleChoiceTestWithQuestionsResponse response = CustomBuilder.buildMultipleChoiceTestWithQuestionsResponse(multipleChoiceTest, questionsOfTheTest);

        // Send notification email to student in this classroom
        mailService.sendTestCreatedNotificationEmail(dto.getClassroomId(), multipleChoiceTest);
        return ResponseEntity.ok(response);
    }

    private List<QuestionResponse> addRandomQuestionToTestByQuestionGroupId
            (Long id, List<CreateMultipleChoiceTestDTO.Questions> randomQuestions) {
        Optional<MultipleChoiceTest> testOp = multipleChoiceTestRepository.findById(id);
        MultipleChoiceTest multipleChoiceTest = testOp.get();
        List<Question> questions = new ArrayList<>();
        List<QuestionResponse> questionResponses = new ArrayList<>();
        randomQuestions.forEach(value->{
            List<Question> questionsTemp = questionService.
                        getRandomQuestionsByQuestionGroup
                                (value.getQuestionGroupId(), value.getNumberOfQuestion());
            questions.addAll(questionsTemp);
        });
        questions.forEach(question -> {
            TestQuestion testQuestion = TestQuestion.builder()
                    .question(question)
                    .multipleChoiceTest(multipleChoiceTest)
                    .build();
            UserProfile userProfile = webUtils.getCurrentLogedInUser();
            testQuestion.setCreatedBy(userProfile.getLoginName());
            testQuestionRepository.save(testQuestion);
            questionResponses.add(CustomBuilder.buildQuestionResponse(question));
        });
        return questionResponses;
    }

    public List<QuestionResponse> addQuestionsToTestByQuestionId(Long id, List<Long> questionIds){
        Optional<MultipleChoiceTest> testOp = multipleChoiceTestRepository.findById(id);
        MultipleChoiceTest multipleChoiceTest = testOp.get();
        List<TestQuestion> testQuestions = new ArrayList<>();
        List<QuestionResponse> questionResponses = new ArrayList<>();
        questionIds.forEach((questionId) -> {
            Optional<Question> questionOp = questionRepository.findById(questionId);
            if (questionOp.isEmpty()) {
                throw new QuestionNotFoundException();
            }
            Question question = questionOp.get();
            TestQuestion testQuestion = TestQuestion.builder()
                    .question(question)
                    .multipleChoiceTest(multipleChoiceTest)
                    .build();
            UserProfile userProfile = webUtils.getCurrentLogedInUser();
            testQuestion.setCreatedBy(userProfile.getLoginName());
            testQuestions.add(testQuestion);
            questionResponses.add(CustomBuilder.buildQuestionResponse(question));
        });
        // Only save when finding all the questions by list questionId
        testQuestionRepository.saveAll(testQuestions);
        return questionResponses;
    }

    private void modifyUpdateMultipleChoiceTest(MultipleChoiceTest multipleChoiceTest) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        multipleChoiceTest.setUpdateBy(userProfile.getLoginName());
        multipleChoiceTest.setUpdateDate(Instant.now());
    }
}
