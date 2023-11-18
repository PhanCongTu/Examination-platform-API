package com.example.springboot.repository;

import com.example.springboot.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long> {

    Optional<Classroom> findByClassCode(String code);

    @Query(value = "select * FROM class_room where (class_name like  :searchText or class_code like :searchText)  and is_enable = :isEnable", nativeQuery = true)
    Page<Classroom> findAllSearchedClassRoomsByStatus(String searchText, Boolean isEnable, Pageable pageable);

    @Query(value = "select * FROM class_room where class_name like :className ", nativeQuery = true)
    Page<Classroom> findAllClassRoomsByClassName(String className, Pageable pageable);

    @Query(value = "select * FROM class_room where class_code like :classCode and is_enable = :isEnable", nativeQuery = true)
    Page<Classroom> findAllClassRoomsByClassCodeAndStatus(String classCode, Boolean isEnable, Pageable pageable);

    @Query(value = "select * FROM class_room where class_code like :classCode", nativeQuery = true)
    Page<Classroom> findAllClassRoomsByClassCode(String classCode, Pageable pageable);

    @Query(value = "select * FROM class_room where id = :classroomId and is_enable = :isEnable", nativeQuery = true)
    Optional<Classroom> findClassRoomByIdAndStatus(Long classroomId, Boolean isEnable);
}