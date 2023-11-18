package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateMultipleChoiceTestDTO;
import com.example.springboot.dto.response.MultipleChoiceTestResponse;
import com.example.springboot.dto.response.MultipleChoiceTestWithQuestionsResponse;
import com.example.springboot.dto.response.QuestionResponse;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.TestQuestion;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.QuestionNotFoundException;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.MultipleChoiceTestRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.repository.TestQuestionRepository;
import com.example.springboot.service.MultipleChoiceTestService;
import com.example.springboot.service.QuestionService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public ResponseEntity<?> createMultipleChoiceTest(CreateMultipleChoiceTestDTO dto) throws QuestionNotFoundException {
        Classroom classroom = classroomRepository.findById(dto.getClassroomId()).get();
        MultipleChoiceTest multipleChoiceTest = new MultipleChoiceTest();
        multipleChoiceTest.setTestName(dto.getTestName());
        multipleChoiceTest.setStartDate(dto.getStartDate());
        multipleChoiceTest.setEndDate(dto.getEndDate());
        multipleChoiceTest.setTestingTime(dto.getTestingTime());
        multipleChoiceTest.setClassRoom(classroom);
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
        MultipleChoiceTestWithQuestionsResponse response = CustomBuilder.builtMultipleChoiceTest(multipleChoiceTest, questionsOfTheTest);
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
            questionResponses.add(CustomBuilder.builtQuestionResponse(question));
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
            questionResponses.add(CustomBuilder.builtQuestionResponse(question));
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
