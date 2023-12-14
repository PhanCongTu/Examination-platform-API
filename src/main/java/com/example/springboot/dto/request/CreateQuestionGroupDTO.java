package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateCreateQuestionGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateCreateQuestionGroup
public class CreateQuestionGroupDTO {
    private String code;
    private String name;
    private Long classroomId;
    private String description;
}
