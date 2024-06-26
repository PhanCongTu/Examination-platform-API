package com.example.springboot.service.impl;

import com.example.springboot.constant.Constants;
import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.AddToClassroomDTO;
import com.example.springboot.dto.request.CreateClassroomDTO;
import com.example.springboot.dto.request.UpdateClassroomDTO;
import com.example.springboot.dto.response.ClassroomResponse;
import com.example.springboot.dto.response.UserProfileResponse;
import com.example.springboot.entity.Classroom;
import com.example.springboot.entity.ClassroomRegistration;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.ClassroomRegistrationRepository;
import com.example.springboot.repository.ClassroomRepository;
import com.example.springboot.repository.StudentRepositoryRead;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.ClassroomService;
import com.example.springboot.util.CustomBuilder;
import com.example.springboot.util.PageUtils;
import com.example.springboot.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final UserProfileRepository userProfileRepository;

    private final ClassroomRepository classRoomRepository;
    private final StudentRepositoryRead studentRepositoryRead;
    private final ClassroomRegistrationRepository classroomRegistrationRepository;
    private final WebUtils webUtils;
    private static final String CODE_PREFIX = "classroom_";

    /**
     * Create a new topic
     *
     * @param DTO : The DTO contains the data
     * @return : The {@link ClassroomResponse}
     */
    @Override
    public ResponseEntity<?> createClassroom(CreateClassroomDTO DTO) {
        log.info("Start create Classroom");
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        Classroom classRoom = new Classroom();
        classRoom.setClassName(DTO.getClassName().trim());
        classRoom.setClassCode(CODE_PREFIX + DTO.getClassCode().trim());
        classRoom.setDescription(DTO.getDescription().trim());
        classRoom.setIsPrivate(DTO.getIsPrivate());
        classRoom.setCreatedBy(userProfile.getLoginName());
        Classroom savedClassroom = classRoomRepository.save(classRoom);
        ClassroomResponse response = CustomBuilder.buildClassroomResponse(savedClassroom);
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
        Optional<Classroom> value = classRoomRepository.findById(classroomID);
        if (value.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        Classroom classRoom = value.get();
        classRoom.setIsEnable(newStatus);
        modifyUpdateClassroom(classRoom);
        classRoomRepository.save(classRoom);
        log.info("End switch Classroom status to " + newStatus);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all the enable Classroom
     *
     * @return the page of DTO response {@link ClassroomResponse}
     */
    @Override
    public ResponseEntity<?> getAllClassroomsByStatus(String search,int page,String column,int size,String sortType, Boolean isEnable) {
        log.info("Start get all enable Classroom (non-Admin)");
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search.trim() + "%";
        Page<Classroom> classRooms = classRoomRepository.findAllSearchedClassRoomsByStatus(searchText,isEnable, pageable);
        // Map topic to topic response DTO
        Page<ClassroomResponse> response = classRooms.map(CustomBuilder::buildClassroomResponse);
        response.forEach((item)->{
            Long total = classroomRegistrationRepository.countAllByClassRoomId(item.getId());
            item.setNumberOfStudents(total);
        });
        log.info("End get all enable Classroom (non-Admin)");
        return ResponseEntity.ok(response);
    }

    /**
     * Update modify information of Classroom
     *
     * @param classRoom the entity
     */
    private void modifyUpdateClassroom(Classroom classRoom) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        classRoom.setUpdateBy(userProfile.getLoginName());
        classRoom.setUpdateDate(Instant.now());
    }


    @Override
    public ResponseEntity<?> updateClassroom(Long classroomId, UpdateClassroomDTO DTO) {
        log.info("Start update Classroom");
        Optional<Classroom> value = classRoomRepository.findById(classroomId);
        if (value.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        Classroom classRoom = value.get();
        if(Objects.nonNull(DTO.getIsPrivate())){
            modifyUpdateClassroom(classRoom);
            classRoom.setIsPrivate(DTO.getIsPrivate());
        }
        if (StringUtils.isNoneBlank(DTO.getClassName())){
            classRoom.setClassName(DTO.getClassName().trim());
            modifyUpdateClassroom(classRoom);
        }
        if (StringUtils.isNoneBlank(DTO.getDescription())){
            classRoom.setDescription(DTO.getDescription().trim());
            modifyUpdateClassroom(classRoom);
        }
        if(StringUtils.isNoneBlank(DTO.getClassCode())){
            Optional<Classroom> classroomEx = classRoomRepository.findByClassCode(DTO.getClassCode().trim());
            if(classroomEx.isPresent() && classroomEx.get().getId() != classroomId){
                LinkedHashMap<String, String> response = new LinkedHashMap<>();
                response.put(Constants.ERROR_CODE_KEY, ErrorMessage.CLASS_CODE_DUPLICATE.getErrorCode());
                response.put(Constants.MESSAGE_KEY, ErrorMessage.CLASS_CODE_DUPLICATE.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response);
            }
            classRoom.setClassCode(DTO.getClassCode().trim());
            modifyUpdateClassroom(classRoom);
        }
        classRoom = classRoomRepository.save(classRoom);
        ClassroomResponse response = CustomBuilder.buildClassroomResponse(classRoom);
        log.info("End update Classroom");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> addStudentToClassroom(AddToClassroomDTO dto) {
        Optional<Classroom> classRoom = classRoomRepository.findById(dto.getClassroomId());
        Optional<UserProfile> userProfile = studentRepositoryRead.findVerifiedStudentByIdAndStatus(dto.getStudentId(), true);

        Optional<ClassroomRegistration> classroomRegistrationExisted =
                classroomRegistrationRepository
                        .findByClassRoomIdAndUserProfileUserID(classRoom.get().getId(), userProfile.get().getUserID());
        if(classroomRegistrationExisted.isPresent()){
            return ResponseEntity.noContent().build();
        }

        ClassroomRegistration classroomRegistration =
                ClassroomRegistration.builder()
                        .classRoom(classRoom.get())
                        .userProfile(userProfile.get())
                        .build();
        ClassroomRegistration savedClassroomRegistration =
                classroomRegistrationRepository.save(classroomRegistration);
        classRoom.get().getClassroomRegistrations().add(savedClassroomRegistration);
        userProfile.get().getClassroomRegistrations().add(savedClassroomRegistration);
        classRoomRepository.save(classRoom.get());
        userProfileRepository.save(userProfile.get());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> getAllStudentOfClassroom(Long classroomId, int page,String column,int size,String sortType) {
        log.info("Start get all user of classroom by id");
        Optional<Classroom> classRoom = classRoomRepository.findById(classroomId);
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        if (classRoom.isEmpty()){
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        Page<UserProfile> listStudentByClassroom =
                studentRepositoryRead.findAllStudentByClassroomId(classroomId, pageable);
        Page<UserProfileResponse> response =
                listStudentByClassroom.map(CustomBuilder::buildUserProfileResponse);
        log.info("End get all user of classroom by id");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getMyClassrooms(String search, int page, String column, int size, String sortType) {
        UserProfile userProfile = webUtils.getCurrentLogedInUser();
        log.info("Get all my classroom. Start. User is: "+userProfile.getUserID());
        Pageable pageable = PageUtils.createPageable(page, size, sortType, column);
        String searchText = "%" + search.trim() + "%";
        Page<Classroom> classRooms = classRoomRepository.findAllRegistedClassroomOfUser(userProfile.getUserID(),searchText, pageable);
        Page<ClassroomResponse> response = classRooms.map(CustomBuilder::buildClassroomResponse);
        response.forEach((item)->{
            Long total = classroomRegistrationRepository.countAllByClassRoomId(item.getId());
            item.setNumberOfStudents(total);
        });
        log.info("Get all my classroom. End. User is: "+userProfile.getUserID());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getClassRoomById(Long classroomId) {
        log.info("Get classroom by id. Start");
        Optional<Classroom> classroom = classRoomRepository.findActiveClassroomById(classroomId);
        if(classroom.isEmpty()) {
            return CustomBuilder.buildClassroomNotFoundResponseEntity();
        }
        ClassroomResponse response = CustomBuilder.buildClassroomResponse(classroom.get());
        Long total = classroomRegistrationRepository.countAllByClassRoomId(classroom.get().getId());
        response.setNumberOfStudents(total);
        log.info("Get classroom by id. End");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> removeStudentFromClassroom(AddToClassroomDTO dto) {
        Optional<Classroom> classRoom = classRoomRepository.findById(dto.getClassroomId());
        Optional<UserProfile> userProfile = studentRepositoryRead.findVerifiedStudentByIdAndStatus(dto.getStudentId(), true);
        Optional<ClassroomRegistration> classroomRegistration = classroomRegistrationRepository.findByClassRoomIdAndUserProfileUserID(classRoom.get().getId(), userProfile.get().getUserID());
        classroomRegistration.ifPresent(registration -> classroomRegistrationRepository.deleteById(registration.getId()));
        return ResponseEntity.noContent().build();
    }
}