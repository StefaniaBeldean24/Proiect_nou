package com.internship.backend.repository;

import com.internship.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String mail);


    @Query(value = "CALL getAllUsersProcedure()", nativeQuery = true)
    List<User> getAllUsersProcedure();

    @Modifying
    @Query(value = "CALL deleteUserByIdProcedure(:userId);", nativeQuery = true)
    void deleteUserByIdProcedure(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Users ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}
