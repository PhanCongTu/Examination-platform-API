package com.example.springboot.repository;

import com.example.springboot.entity.ClassroomRegistration;
import com.example.springboot.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRegistrationRepository extends JpaRepository<ClassroomRegistration,Long> {


}
