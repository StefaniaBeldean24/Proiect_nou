package com.internship.backend.repository;

import com.internship.backend.model.Location;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;


public interface LocationRepository extends CouchbaseRepository<Location, String> {
    Optional<Location> findByName(String name);
}