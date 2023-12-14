package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateUpdateMultipleChoiceTest;
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
@ValidateUpdateMultipleChoiceTest
public class UpdateMultipleChoiceTestDTO {
    private String testName;
    private String description;
    // milliseconds since January 1, 1970
    private Long startDate;
    // milliseconds since January 1, 1970
    private Long endDate;
    // minutes
    private Long testingTime;
}
