package com.platform.repository;

import com.platform.domain.Tracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Tracker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackerRepository extends MongoRepository<Tracker, String> {}
