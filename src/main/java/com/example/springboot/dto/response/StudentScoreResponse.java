package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class StudentScoreResponse {
    private Long id;
    private Long testId;
    private Double totalScore;
    private Instant submittedDate;
    private Boolean isLate;
    private Long studentId;
    private String studentDisplayName;
    private String studentLoginName;

    public StudentScoreResponse(Long id, Long testId, Double totalScore, Instant submittedDate, Boolean isLate, Long studentId, String studentDisplayName, String studentLoginName) {
        this.id = id;
        this.testId = testId;
        this.totalScore = totalScore;
        this.submittedDate = submittedDate;
        this.isLate = isLate;
        this.studentId = studentId;
        this.studentDisplayName = studentDisplayName;
        this.studentLoginName = studentLoginName;
    }
}
