package com.example.springboot.repository;

import com.example.springboot.entity.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<ClassRoom,Long> {

    Optional<ClassRoom> findByClassCode(String code);

    @Query(value = "select * FROM class_room where (class_name like  :searchText or class_code like :searchText)  and is_enable = :isEnable", nativeQuery = true)
    Page<ClassRoom> findAllSearchedClassRoomsByStatus(String searchText, Boolean isEnable, Pageable pageable);

    @Query(value = "select * FROM class_room where class_name like :className ", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassName(String className, Pageable pageable);

    @Query(value = "select * FROM class_room where class_code like :classCode and is_enable = :isEnable", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassCodeAndStatus(String classCode, Boolean isEnable, Pageable pageable);

    @Query(value = "select * FROM class_room where class_code like :classCode", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassCode(String classCode, Pageable pageable);

    @Query(value = "select * FROM class_room where id :classroomId and is_enable = :isEnable", nativeQuery = true)
    Optional<ClassRoom> findClassRoomsByIdAndStatus(Long classroomId, Boolean isEnable);
}