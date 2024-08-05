package com.internship.backend.repository;

import com.internship.backend.model.Authority;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;


public interface AuthorityRepository extends CouchbaseRepository<Authority, String> {

    Optional<Authority> findByName(String name);
}