package com.internship.backend.repository;

<<<<<<< Updated upstream
import com.couchbase.client.java.query.QueryScanConsistency;
import com.internship.backend.model.Users;
=======
import com.internship.backend.model.User;
>>>>>>> Stashed changes

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<Users, Integer> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String mail);
}