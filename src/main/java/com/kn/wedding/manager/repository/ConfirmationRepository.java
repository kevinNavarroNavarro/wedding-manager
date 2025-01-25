package com.kn.wedding.manager.repository;

import com.kn.wedding.manager.entity.Confirmation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationRepository extends MongoRepository<Confirmation, Integer> {
    Optional<Confirmation> findByGuestCode(UUID guestCode);
}