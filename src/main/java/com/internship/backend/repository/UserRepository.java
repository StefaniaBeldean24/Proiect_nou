package com.internship.backend.repository;

import com.internship.backend.model.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String mail);
}