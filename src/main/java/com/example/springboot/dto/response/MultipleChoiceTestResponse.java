package com.example.springboot.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class MultipleChoiceTestResponse {
    private Long id;

    private String testName;

    private String description;

    private Long startDate;

    private Long endDate;

    private Long testingTime;

    private double targetScore;
}
