package com.internship.backend.repository;

import com.internship.backend.model.TennisCourt;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface TennisCourtRepository extends CouchbaseRepository<TennisCourt, String> {
    Optional<TennisCourt> findByName(String name);
}