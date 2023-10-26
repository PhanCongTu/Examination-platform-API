package com.example.springboot.repository;

import com.example.springboot.entity.ClassRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {

    Optional<ClassRoom> findByClassCode(String code);

    @Query(value = "select * FROM class_room where class_name like %:className% and is_enable = :isEnable", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassNameAndStatus(String className, Boolean isEnable, Pageable pageable);

    @Query(value = "select * FROM class_room where class_name like %:className% ", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassName(String className, Pageable pageable);

    @Query(value = "select * FROM class_room where class_code like %:classCode% and is_enable = :isEnable", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassCodeAndStatus(String classCode, Boolean isEnable, Pageable pageable);

    @Query(value = "select * FROM class_room where class_code like %:classCode%", nativeQuery = true)
    Page<ClassRoom> findAllClassRoomsByClassCode(String classCode, Pageable pageable);
}