package com.internship.backend.repository;

import com.internship.backend.model.TennisCourt;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface TennisCourtRepository extends CouchbaseRepository<TennisCourt, Integer> {
}