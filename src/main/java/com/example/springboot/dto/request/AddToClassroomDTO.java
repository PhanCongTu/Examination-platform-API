package com.example.springboot.dto.request;


import com.example.springboot.validate.ValidateAddToClassroom;
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
@ValidateAddToClassroom
public class AddToClassroomDTO {
    private Long classroomId;
    private Long studentId;
}
