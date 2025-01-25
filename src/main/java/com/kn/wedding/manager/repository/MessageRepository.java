package com.kn.wedding.manager.repository;

import com.kn.wedding.manager.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends MongoRepository<Message, Integer> {
    Optional<Message> findByGuestCode(UUID guestCode);
}