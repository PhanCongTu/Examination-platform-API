package com.example.springboot.service;

import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import org.springframework.http.ResponseEntity;

public interface ClassroomService {

    ResponseEntity<?> createClassroom(CreateClassroomDTO DTO);

    ResponseEntity<?> switchClassroomStatus(Long topicId, Boolean newStatus);

    ResponseEntity<?> getAllClassroomsByStatus(String search,int page,String column,int size,String sortType, Boolean enable);

    ResponseEntity<?> updateClassroom(Long classroomId, UpdateClassroomDTO DTO);
}