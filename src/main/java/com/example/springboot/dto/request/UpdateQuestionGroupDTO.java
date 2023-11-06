package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateUpdateQuestionGroup;
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
@ValidateUpdateQuestionGroup
public class UpdateQuestionGroupDTO {
    private String code;
    private String name;
}
