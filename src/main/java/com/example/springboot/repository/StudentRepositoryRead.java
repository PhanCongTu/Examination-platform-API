package com.example.springboot.repository;

import com.example.springboot.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepositoryRead extends JpaRepository<UserProfile, Long> {
    @Query(value = "Select * from user_profile u\n" +
            "\twhere u.is_enable = :isActive and u.user_id in (\n" +
            "    select user_profile_user_id from user_profile_roles ur\n" +
            "\t\twhere ur.user_profile_user_id = :userId and is_email_address_verified=true and ur.roles = \"ROLE_STUDENT\"\n" +
            "    )",
            nativeQuery = true)
    Optional<UserProfile> findVerifiedStudentByIdAndStatus(Long userId, Boolean isActive);

    @Query(value = "select * from user_profile \n" +
            "where user_id in (\n" +
            "\t select user_profile_user_id \n" +
            "    from classroom_registration \n" +
            "    where class_room_id = :classroomId \n" +
            ")", nativeQuery = true)
    Page<UserProfile> findAllStudentByClassroomId(Long classroomId, Pageable pageable);

    @Query(value = "Select * from user_profile u\n" +
            "\twhere (display_name like :searchText or email_address like :searchText) and u.is_enable = :isActive \n" +
            "\t\tand u.user_id in (\n" +
            "\t\tselect user_profile_user_id from user_profile_roles ur\n" +
            "\t\t\twhere ur.roles = \"ROLE_STUDENT\"\n" +
            "\t\t)",
            nativeQuery = true)
    Page<UserProfile> findAllSeachedStudentsByStatus(String searchText, Boolean isActive, Pageable pageable);
}
