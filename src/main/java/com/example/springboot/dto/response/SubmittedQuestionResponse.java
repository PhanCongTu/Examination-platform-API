package com.example.springboot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmittedQuestionResponse {
    private Long id;
    private Long questionId;
    private String content;
    private String firstAnswer;
    private String secondAnswer;
    private String thirdAnswer;
    private String fourthAnswer;
    private String correctAnswer;
    private String submittedAnswer;
}
