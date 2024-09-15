package com.kn.wedding.manager.dto;

public record ConfirmationView(Integer id, Integer guestId, String guestName, Integer totalReservedSeats, String willAttend) {
}
