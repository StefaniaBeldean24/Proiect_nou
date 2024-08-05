package com.internship.backend.repository;

import com.internship.backend.model.Price;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface PriceRepository extends CouchbaseRepository<Price, String> {
    Optional<Price> findPriceByTennisCourtName(String tennisCourtName);
}