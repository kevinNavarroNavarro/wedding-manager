package com.kn.wedding.manager.dto;

import java.util.UUID;

public record GuestView(UUID code, String name, Integer availableSeats, boolean hasConfirmed, String message) {
}
