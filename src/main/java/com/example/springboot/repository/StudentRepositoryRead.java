package com.example.springboot.repository;

import com.example.springboot.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepositoryRead extends JpaRepository<UserProfile, Long> {
    @Query(value = "SELECT * FROM user_profile u \n" +
            "\tinner join user_profile_roles ur \n" +
            "\ton u.user_id = ur.user_profile_user_id\n" +
            "where u.user_id = :userId and u.is_enable = :isActive and ur.roles = \"ROLE_STUDENT\";",
            nativeQuery = true)
    Optional<UserProfile> findStudentByIdAndStatus(Long userId, Boolean isActive);

    @Query(value = "select * from user_profile \n" +
            "where user_id in (\n" +
            "\t select user_profile_user_id \n" +
            "    from classroom_registration \n" +
            "    where class_room_id = :classroomId \n" +
            ")", nativeQuery = true)
    List<UserProfile> findAllStudentByClassroomId(Long classroomId);
}
