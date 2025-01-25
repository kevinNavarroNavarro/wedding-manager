package com.kn.wedding.manager.dto;

import org.bson.types.ObjectId;

import java.util.UUID;

public record ConfirmationView(ObjectId id, UUID guestCode, String guestName, Integer totalReservedSeats, String willAttend) {
}
