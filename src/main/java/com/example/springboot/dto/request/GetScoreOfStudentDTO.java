package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateGetScoreOfStudent;
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
@ValidateGetScoreOfStudent
public class GetScoreOfStudentDTO {
    private Long studentId;
    private Long multipleChoiceTestId;
}
