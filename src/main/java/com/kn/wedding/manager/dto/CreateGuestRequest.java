package com.kn.wedding.manager.dto;

import com.kn.wedding.manager.entity.Guest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateGuestRequest(List<GuestRequest> guests) {
    public record GuestRequest(@NotNull String name, @NotNull Integer availableSeats) {
        public Guest toGuest(UUID code) {
            Guest guest = new Guest();
            guest.setName(name);
            guest.setAvailableSeats(availableSeats);
            guest.setCode(code);

            return guest;
        }
    }
}
