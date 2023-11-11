package com.example.springboot.service.impl;

import com.example.springboot.dto.request.CreateQuestionDTO;
import com.example.springboot.dto.response.QuestionResponse;
import com.example.springboot.entity.Question;
import com.example.springboot.entity.QuestionGroup;
import com.example.springboot.repository.QuestionGroupRepository;
import com.example.springboot.repository.QuestionRepository;
import com.example.springboot.service.QuestionService;
import com.example.springboot.util.CustomBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    
    @Override
    public ResponseEntity<?> createQuestion(CreateQuestionDTO dto) {
        log.info("Start create Question");
        Optional<QuestionGroup> questionGroupOp =
                questionGroupRepository.findByIdAndStatus(dto.getQuestionGroupId(), true);

        Question question = Question.builder()
                .content(dto.getContent())
                .firstAnswer(dto.getFirstAnswer().getAnswerContent())
                .secondAnswer(dto.getSecondAnswer().getAnswerContent())
                .thirdAnswer(dto.getThirdAnswer().getAnswerContent())
                .fourthAnswer(dto.getFourthAnswer().getAnswerContent())
                .correctAnswer(getCorrectAnswer(dto))
                .questionGroup(questionGroupOp.get())
                .build();
        question = questionRepository.save(question);
        QuestionResponse response = CustomBuilder.builtQuestionResponse(question);
        log.info("End create Question");
        return ResponseEntity.ok(response);
    }
    private String getCorrectAnswer(CreateQuestionDTO dto){
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
