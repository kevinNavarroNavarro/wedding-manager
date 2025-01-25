package com.kn.wedding.manager.repository;

import com.kn.wedding.manager.entity.Guest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuestRepository extends MongoRepository<Guest, ObjectId> {
    Optional<Guest> findByCode(UUID code);
    boolean existsByCode(UUID code);
}