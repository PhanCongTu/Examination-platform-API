package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateCreateMultipleChoiceTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateCreateMultipleChoiceTest
public class CreateMultipleChoiceTestDTO {
    private String testName;
    // milliseconds since January 1, 1970
    private Long startDate;
    // milliseconds since January 1, 1970
    private Long endDate;
    // minutes
    private Long testingTime;

    private Long classroomId;

    private List<Long> questionIds;

    private List<Questions> randomQuestions;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Questions {
        private Long questionGroupId;
        private Long numberOfQuestion;
    }
}
