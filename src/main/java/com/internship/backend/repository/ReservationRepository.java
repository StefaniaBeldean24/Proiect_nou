package com.internship.backend.repository;

import com.internship.backend.model.Reservation;
import org.springframework.data.couchbase.repository.CouchbaseRepository;


public interface ReservationRepository extends CouchbaseRepository<Reservation, String> {
}