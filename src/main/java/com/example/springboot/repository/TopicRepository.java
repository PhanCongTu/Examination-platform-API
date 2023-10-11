package com.example.springboot.repository;

import com.example.springboot.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {

    Optional<Topic> findByCode(String code);

    @Query(value = "select * FROM topic where is_enable = true", nativeQuery = true)
    List<Topic> findAllEnableTopics(Pageable pageable);
}
