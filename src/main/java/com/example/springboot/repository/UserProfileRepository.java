package com.example.springboot.repository;

import com.example.springboot.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query(value = "SELECT * FROM user_profile where login_name = :loginName",
            nativeQuery = true)
    Optional<UserProfile> findOneByLoginName(String loginName);
    @Query(value = "SELECT * FROM user_profile where login_name = :loginName and hash_password = :hasPassword",
            nativeQuery = true)
    Optional<UserProfile> findOneByLoginNameAndHashPassword(String loginName, String hasPassword);
    @Query(value = "SELECT * FROM user_profile where email_address = :emailAddress and is_email_address_verified = true",
            nativeQuery = true)
    Optional<UserProfile> findOneByEmailAddressVerified(String emailAddress);


    Optional<UserProfile> findOneByLoginNameOrEmailAddressAndIsEmailAddressVerified(String loginName, String emailAddress, Boolean isEmailAddressVerified);

}
