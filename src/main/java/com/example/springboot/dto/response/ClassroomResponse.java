package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ClassroomResponse {
    private Long id;
    private String className;
    private String classCode;
    private Boolean isEnable;
    private Boolean isPrivate;
    private String description;

    public ClassroomResponse(Long id, String className, String classCode, Boolean isEnable, Boolean isPrivate, String description) {
        this.id = id;
        this.className = className;
        this.classCode = classCode;
        this.isEnable = isEnable;
        this.isPrivate = isPrivate;
        this.description = description;
    }
}