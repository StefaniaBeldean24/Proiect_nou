package com.internship.backend.repository;

import com.internship.backend.model.Price;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface PriceRepository extends CouchbaseRepository<Price, Integer> {
}