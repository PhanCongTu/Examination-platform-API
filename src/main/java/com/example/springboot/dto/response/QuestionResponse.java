package com.example.springboot.dto.response;

import com.example.springboot.dto.request.CreateQuestionDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class QuestionResponse {
    private Long id;
    private String content;
    private String firstAnswer;
    private String secondAnswer;
    private String thirdAnswer;
    private String fourthAnswer;
}
