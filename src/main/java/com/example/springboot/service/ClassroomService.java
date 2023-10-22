package com.example.springboot.service;

import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import org.springframework.http.ResponseEntity;

public interface ClassroomService {

    ResponseEntity<?> createClassroom(CreateClassroomDTO DTO);

    ResponseEntity<?> switchClassroomStatus(Long topicId, Boolean newStatus);

    ResponseEntity<?> getAllEnableClassrooms(Integer pageNumber);

    ResponseEntity<?> updateClassroom(Long classroomId, UpdateClassroomDTO DTO);
}
