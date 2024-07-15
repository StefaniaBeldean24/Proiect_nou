package com.internship.backend.repository;

import com.internship.backend.model.Location;

import org.springframework.data.couchbase.repository.CouchbaseRepository;


public interface LocationRepository extends CouchbaseRepository<Location, Integer> {
    Location findByName(String name);
}