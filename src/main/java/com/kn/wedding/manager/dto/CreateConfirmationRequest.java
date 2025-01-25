package com.kn.wedding.manager.dto;

import com.kn.wedding.manager.entity.Confirmation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.kn.wedding.manager.entity.Confirmation.*;

public record CreateConfirmationRequest(@NotNull UUID guestCode, @NotNull Integer totalReservedSeats, @NotNull String willAttend) {

    public Confirmation toConfirmation() {
        Confirmation confirmation = new Confirmation();
        confirmation.setGuestCode(guestCode);
        confirmation.setReservedSeats(totalReservedSeats);
        confirmation.setWillAttend(willAttend);

        return confirmation;
    }
}
