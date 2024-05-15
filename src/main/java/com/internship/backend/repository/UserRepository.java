package com.internship.backend.repository;

import com.internship.backend.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);
    Users findByEmail(String mail);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Users ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}
