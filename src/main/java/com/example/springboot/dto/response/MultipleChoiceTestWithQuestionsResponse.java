package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@SuperBuilder
public class MultipleChoiceTestWithQuestionsResponse extends MultipleChoiceTestResponse {
    List<QuestionResponse> questions;
}
