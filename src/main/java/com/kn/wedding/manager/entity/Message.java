package com.kn.wedding.manager.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "message")
public class Message {
    private ObjectId id;
    private UUID guestCode;
    private String description;
}
