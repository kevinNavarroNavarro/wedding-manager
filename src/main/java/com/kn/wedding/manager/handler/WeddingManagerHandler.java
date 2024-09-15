package com.kn.wedding.manager.handler;

import com.kn.wedding.manager.dto.CreateConfirmationRequest;
import com.kn.wedding.manager.service.WeddingManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
class WeddingManagerHandler {
    private final WeddingManagerService managerService;

    public void insertConfirmation(CreateConfirmationRequest request) {
        var availableSeats = this.managerService.getGuestInformation(request.guestId());
        Assert.isTrue(availableSeats.getAvailableSeats() >= request.totalReservedSeats(),
                "Reserved seats exceeds available seats");

        this.managerService.insertOrUpdateConfirmation(request.toConfirmation());
    }
}

