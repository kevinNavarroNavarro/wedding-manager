package com.kn.wedding.manager.dto;

import com.kn.wedding.manager.entity.Confirmation;
import org.jetbrains.annotations.NotNull;

import static com.kn.wedding.manager.entity.Confirmation.*;

public record CreateConfirmationRequest(@NotNull Integer guestId, @NotNull Integer totalReservedSeats, @NotNull Attend willAttend) {

    public Confirmation toConfirmation() {
        Confirmation confirmation = new Confirmation();
        confirmation.setGuestId(guestId);
        confirmation.setReservedSeats(totalReservedSeats);
        confirmation.setWillAttend(willAttend);

        return confirmation;
    }
}
