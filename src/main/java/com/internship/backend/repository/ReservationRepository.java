package com.internship.backend.repository;

import com.internship.backend.model.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE Reservation ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void resetAutoIncrementId();
}