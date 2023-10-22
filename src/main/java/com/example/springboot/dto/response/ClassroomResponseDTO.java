package com.example.springboot.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ClassroomResponseDTO {
    private Long id;
    private String className;
    private String classCode;
    private Boolean isActive;
    private Boolean isPrivate;

    public ClassroomResponseDTO(Long id, String className, String classCode, Boolean isActive, Boolean isPrivate) {
        this.id = id;
        this.className = className;
        this.classCode = classCode;
        this.isActive = isActive;
        this.isPrivate = isPrivate;
    }
}
