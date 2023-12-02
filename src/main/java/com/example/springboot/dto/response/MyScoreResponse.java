package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class MyScoreResponse {
    private Long id;
    private Double totalScore;
    private Boolean isLate;
    private Long submittedDate;
    private Long testId;
    private String testName;
    private Long classroomId;
    private String className;
    private String classCode;

    public MyScoreResponse(Long id, Double totalScore, Boolean isLate, Long submittedDate, Long testId, String testName, Long classroomId, String className, String classCode) {
        this.id = id;
        this.totalScore = totalScore;
        this.isLate = isLate;
        this.submittedDate = submittedDate;
        this.testId = testId;
        this.testName = testName;
        this.classroomId = classroomId;
        this.className = className;
        this.classCode = classCode;
    }
}
