package com.internship.backend.repository;

import com.internship.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String mail);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Users ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}
