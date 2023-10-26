package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import com.example.springboot.dto.response.ClassroomResponseDTO;
import com.example.springboot.entity.ClassRoom;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.ClassRoomRepository;
import com.example.springboot.service.ClassroomService;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassRoomRepository classRoomRepository;
    private final WebUtils webUtils;
    private static final String CODE_PREFIX = "classroom_";
    private static final Integer PAGE_SIZE = 1;
    private static final String PAGE_SORT_BY = "class_code";

    /**
     * Create a new topic
     *
     * @param DTO : The DTO contains the data
     * @return : The {@link ClassroomResponseDTO}
     */
    @Override
    public ResponseEntity<?> createClassroom(CreateClassroomDTO DTO) {
        log.info("Start create Classroom");
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        ClassRoom classRoom = new ClassRoom();
        classRoom.setClassName(DTO.getClassName());
        classRoom.setClassCode(CODE_PREFIX + DTO.getClassCode());
        classRoom.setIsPrivate(DTO.getIsPrivate());
        classRoom.setCreatedBy(userProfile.getLoginName());
        ClassRoom savedClassRoom = classRoomRepository.save(classRoom);
        ClassroomResponseDTO response = ClassroomResponseDTO.builder()
                .id(savedClassRoom.getId())
                .className(savedClassRoom.getClassName())
                .classCode(savedClassRoom.getClassCode())
                .isPrivate(savedClassRoom.getIsPrivate())
                .isActive(savedClassRoom.getIsEnable())
                .build();
        log.info("End create Classroom");
        return ResponseEntity.ok(response);
    }


    /**
     * Change status of the topic by id
     *
     * @param classroomID : the topic id
     * @param newStatus : new boolean status
     * @return : no content response
     */
    @Override
    public ResponseEntity<?> switchClassroomStatus(Long classroomID, Boolean newStatus) {
        log.info("Start switch Classroom status to " + newStatus);
        Optional<ClassRoom> value = classRoomRepository.findById(classroomID);
        if (value.isEmpty()){
            return buildClassroomNotFound();
        }
        ClassRoom classRoom = value.get();
        classRoom.setIsEnable(newStatus);
        modifyUpdateClassroom(classRoom);
        classRoomRepository.save(classRoom);
        log.info("End switch Classroom status to " + newStatus);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all the enable Classroom
     *
     * @return the page of DTO response {@link ClassroomResponseDTO}
     */
    @Override
    public ResponseEntity<?> getAllClassroomsByStatus(String search,int page,String column,int size,String sortType, Boolean isEnable) {
        log.info("Start get all enable Classroom (non-Admin)");
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        Page<ClassRoom> classRooms = classRoomRepository.findAllClassRoomsByClassNameAndStatus(search,isEnable, pageable);
        // Map topic to topic response DTO
        Page<ClassroomResponseDTO> response = classRooms.map(classRoom -> new ClassroomResponseDTO(
                classRoom.getId(),
                classRoom.getClassCode(),
                classRoom.getClassName(),
                classRoom.getIsEnable(),
                classRoom.getIsPrivate()
        ));
        log.info("End get all enable Classroom (non-Admin)");
        return ResponseEntity.ok(response);
    }

    /**
     * Update modify information of Classroom
     *
     * @param classRoom the entity
     */
    private void modifyUpdateClassroom(ClassRoom classRoom) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        classRoom.setUpdateBy(userProfile.getLoginName());
        classRoom.setUpdateDate(Instant.now());
    }

    /**
     * Build an error response when the Classroom is not found
     *
     * @return the response
     */
    private ResponseEntity<LinkedHashMap<String, String>> buildClassroomNotFound() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(Constants.ERROR_CODE_KEY, ErrorMessage.CLASSROOM_NOT_FOUND.getErrorCode());
        response.put(Constants.MESSAGE_KEY, ErrorMessage.CLASSROOM_NOT_FOUND.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Override
    public ResponseEntity<?> updateClassroom(Long classroomId, UpdateClassroomDTO DTO) {
        log.info("Start update Classroom");
        Optional<ClassRoom> value = classRoomRepository.findById(classroomId);
        if (value.isEmpty()){
            return buildClassroomNotFound();
        }
        ClassRoom classRoom = value.get();
        if(Objects.nonNull(DTO.getIsPrivate())){
            modifyUpdateClassroom(classRoom);
            classRoom.setIsPrivate(DTO.getIsPrivate());
        }
        if (Objects.nonNull(DTO.getClassName())){
            classRoom.setClassName(DTO.getClassName());
            modifyUpdateClassroom(classRoom);
        }
        if(Objects.nonNull(DTO.getClassCode())){
            classRoom.setClassCode(CODE_PREFIX + DTO.getClassCode());
            modifyUpdateClassroom(classRoom);
        }
        classRoomRepository.save(classRoom);
        log.info("End update Classroom");
        return ResponseEntity.noContent().build();
    }
}