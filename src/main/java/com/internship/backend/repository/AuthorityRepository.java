package com.internship.backend.repository;

import com.internship.backend.model.Authority;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Modifying
    @Query("DELETE FROM Authority a where a.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Authority ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}
