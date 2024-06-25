package com.internship.backend.repository;

import com.internship.backend.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    Users findByEmail(String mail);

    @Query(value = "CALL getAllUsersProcedure()", nativeQuery = true)
    List<Users> getAllUsersProcedure();

    @Modifying
    @Query(value = "CALL deleteUserByIdProcedure(:userId);", nativeQuery = true)
    void deleteUserByIdProcedure(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Users ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}
