package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateCreateClassroom;
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
@ValidateCreateClassroom
public class CreateClassroomDTO {
    private String classCode;
    private String className;
    private Boolean isPrivate;
}