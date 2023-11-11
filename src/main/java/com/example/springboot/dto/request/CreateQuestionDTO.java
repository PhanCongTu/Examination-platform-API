package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateCreateQuestion;
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
@ValidateCreateQuestion
public class CreateQuestionDTO {
    private String content;
    private Answer firstAnswer;
    private Answer secondAnswer;
    private Answer thirdAnswer;
    private Answer fourthAnswer;
    private Long questionGroupId;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        private String answerContent;
        private Boolean isCorrect;
    }
}
