package com.internship.backend.repository;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.internship.backend.model.Users;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<Users, Integer> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String mail);
}