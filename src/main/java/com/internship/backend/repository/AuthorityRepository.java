package com.internship.backend.repository;

import com.internship.backend.model.Authority;

import com.internship.backend.model.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;


public interface AuthorityRepository extends CouchbaseRepository<Authority, Integer> {

    Optional<Authority> findByName(String name);

//    Optional<Users> findByUsername(String username);
}