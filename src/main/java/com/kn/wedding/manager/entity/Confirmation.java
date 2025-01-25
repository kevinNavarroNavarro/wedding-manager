package com.kn.wedding.manager.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "confirmation")
public class Confirmation {
    @Id
    private ObjectId id;
    private UUID guestCode;
    private Integer reservedSeats;
    private String willAttend;
    private Date confirmedAt;
}
