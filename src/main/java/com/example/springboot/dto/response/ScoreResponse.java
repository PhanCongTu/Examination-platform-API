package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ScoreResponse {
    private Long id;
    private Double totalScore;
    private Long SubmittedDate;
    private Boolean isLate;
    private MultipleChoiceTestResponse multipleChoiceTest;
    private List<SubmittedQuestionResponse> submittedQuestions;
}
