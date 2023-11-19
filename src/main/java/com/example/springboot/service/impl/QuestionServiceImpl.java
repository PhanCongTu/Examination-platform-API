package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.request.UpdateQuestionDTO;
import com.example.springboot.dto.response.QuestionResponse;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.NotEnoughQuestionException;
import com.example.springboot.exception.QuestionGroupNotFoundException;
import com.example.springboot.repository.QuestionGroupRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.service.QuestionService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    private final WebUtils webUtils;

    @Override
    public List<Question> getRandomQuestionsByQuestionGroup(Long questionGroupId, Long numberOfQuestion) {
        Optional<QuestionGroup> questionGroup = questionGroupRepository
                .findByIdAndStatus(questionGroupId, true);
        if(questionGroup.isEmpty()) {
            throw new QuestionGroupNotFoundException();
        }
        List<Question> questions =  questionRepository
                .findRandomQuestionByQuestionGroupId(questionGroupId, numberOfQuestion);
        if (questions.size() < numberOfQuestion) {
            throw new NotEnoughQuestionException(questionGroup.get().getName());
        }
        return questions;
    }
    
    @Override
    public ResponseEntity<?> createQuestion(CreateQuestionDTO dto) {
        log.info("Create question: start");
        Optional<QuestionGroup> questionGroupOp =
                questionGroupRepository.findByIdAndStatus(dto.getQuestionGroupId(), true);

        Question question = Question.builder()
                .content(dto.getContent())
                .firstAnswer(dto.getFirstAnswer().getAnswerContent())
                .secondAnswer(dto.getSecondAnswer().getAnswerContent())
                .thirdAnswer(dto.getThirdAnswer().getAnswerContent())
                .fourthAnswer(dto.getFourthAnswer().getAnswerContent())
                .correctAnswer(getCorrectAnswerCreate(dto))
                .questionGroup(questionGroupOp.get())
                .build();
        modifyUpdateQuestion(question);
        question = questionRepository.save(question);
        QuestionResponse response = CustomBuilder.buildQuestionResponse(question);
        log.info("Create question: end");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateQuestion(Long questionId, UpdateQuestionDTO dto) {
        log.info("Update question: start");
        Optional<Question> questionOp = questionRepository.findByIdAndStatus(questionId, true);
        if(questionOp.isEmpty()) {
            return CustomBuilder.buildQuestionNotFoundResponseEntity();
        }

        Question question = questionOp.get();
        String correctAnswer = question.getCorrectAnswer();

        if(Objects.nonNull(dto.getContent())) {
            question.setContent(dto.getContent());
            modifyUpdateQuestion(question);
        }
        if(Objects.nonNull(dto.getFirstAnswer())) {
            String answerContent = dto.getFirstAnswer().getAnswerContent();
            question.setFirstAnswer(answerContent);
            if(dto.getFirstAnswer().getIsCorrect()) {
                correctAnswer = answerContent;
            }
            modifyUpdateQuestion(question);
        }
        if(Objects.nonNull(dto.getSecondAnswer())) {
            String answerContent = dto.getSecondAnswer().getAnswerContent();
            question.setSecondAnswer(answerContent);
            if(dto.getSecondAnswer().getIsCorrect()) {
                correctAnswer = answerContent;
            }
            modifyUpdateQuestion(question);
        }
        if(Objects.nonNull(dto.getThirdAnswer())) {
            String answerContent = dto.getThirdAnswer().getAnswerContent();
            question.setThirdAnswer(answerContent);
            if(dto.getThirdAnswer().getIsCorrect()) {
                correctAnswer = answerContent;
            }
            modifyUpdateQuestion(question);
        }
        if(Objects.nonNull(dto.getFourthAnswer())) {
            String answerContent = dto.getFourthAnswer().getAnswerContent();
            question.setFourthAnswer(answerContent);
            if(dto.getFourthAnswer().getIsCorrect()) {
                correctAnswer = answerContent;
            }
            modifyUpdateQuestion(question);
        }
        question.setCorrectAnswer(correctAnswer);
        question = questionRepository.save(question);
        QuestionResponse response = CustomBuilder.buildQuestionResponse(question);
        log.info("Update question: end");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> switchQuestionStatus(Long questionId, boolean newStatus) {
        log.info(String.format("Switch question status to %s: %s", newStatus, "start"));
        Optional<Question> questionOp = questionRepository.findById(questionId);
        if(questionOp.isEmpty()) {
            return CustomBuilder.buildQuestionNotFoundResponseEntity();
        }
        questionOp.get().setIsEnable(newStatus);
        modifyUpdateQuestion(questionOp.get());
        questionRepository.save(questionOp.get());
        log.info(String.format("Switch question status to %s: %s", newStatus, "end"));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> getAllQuestionOfQuestionGroup(Long questionGroupId,String search, int page, String column, int size, String sortType, boolean isActiveQuestion) {
        log.info("Get questions of question group: start, isActiveQuestion: "+isActiveQuestion);
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search + "%";
        Optional<QuestionGroup> questionGroupOp =
                questionGroupRepository.findByIdAndStatus(questionGroupId, true);
        if (questionGroupOp.isEmpty()) {
            return CustomBuilder.buildQuestionGroupNotFoundResponseEntity();
        }
        Page<Question> questions = questionRepository
                .getQuestionsOfQuestionGroupByQuestionGroupId(questionGroupId, searchText, isActiveQuestion, pageable);
        Page<QuestionResponse> response = questions.map(CustomBuilder::buildQuestionResponse);
        log.info("Get questions of question group: end, isActiveQuestion: "+isActiveQuestion);
        return ResponseEntity.ok(response);
    }

    private void modifyUpdateQuestion(Question question) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        question.setUpdateBy(userProfile.getLoginName());
        question.setUpdateDate(Instant.now());
    }

    private String getCorrectAnswerCreate(CreateQuestionDTO dto){
        if (dto.getFirstAnswer().getIsCorrect()){
            return dto.getFirstAnswer().getAnswerContent();
        }
        if (dto.getSecondAnswer().getIsCorrect()){
            return dto.getSecondAnswer().getAnswerContent();
        }
        if (dto.getThirdAnswer().getIsCorrect()){
            return dto.getThirdAnswer().getAnswerContent();
        }
        if (dto.getFourthAnswer().getIsCorrect()){
            return dto.getFourthAnswer().getAnswerContent();
        }
        log.error("There are no correct answer in CreateQuestionDTO");
        return "";
    }
}
