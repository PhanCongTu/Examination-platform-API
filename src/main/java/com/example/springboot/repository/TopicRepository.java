package com.example.springboot.repository;

import com.example.springboot.dto.response.TopicResponseDTO;
import com.example.springboot.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {
    Optional<Topic> findByCode(String code);

    @Query(value = "SELECT id, topic_name, code FROM topic where is_enable = true", nativeQuery = true)
    List<Object> findAllTopics();
}
