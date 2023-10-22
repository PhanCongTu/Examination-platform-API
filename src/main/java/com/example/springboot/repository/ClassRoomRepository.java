package com.example.springboot.repository;

import com.example.springboot.entity.ClassRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {

    Optional<ClassRoom> findByClassCode(String code);

    @Query(value = "select * FROM class_room where is_enable = true", nativeQuery = true)
    List<ClassRoom> findAllEnableClassRooms(Pageable pageable);
}
