package com.internship.backend.repository;

import com.internship.backend.model.Reservation;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;


public interface ReservationRepository extends CouchbaseRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Integer userId);
}