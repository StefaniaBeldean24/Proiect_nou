package com.internship.backend.repository;

import com.internship.backend.model.Price;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Price ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}
