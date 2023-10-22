package com.example.springboot.dto.request;

import com.example.springboot.validate.ValidateUpdateClassroom;
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
@ValidateUpdateClassroom
public class UpdateClassroomDTO {
    private String classCode;
    private String className;
    private Boolean isPrivate;
}
