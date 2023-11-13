package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateUpdateQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateUpdateQuestion
public class UpdateQuestionDTO {
    private String content;
    private CreateQuestionDTO.Answer firstAnswer;
    private CreateQuestionDTO.Answer secondAnswer;
    private CreateQuestionDTO.Answer thirdAnswer;
    private CreateQuestionDTO.Answer fourthAnswer;
}
