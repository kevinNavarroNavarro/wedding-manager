package com.kn.wedding.manager.handler;

import com.kn.wedding.manager.dto.*;
import com.kn.wedding.manager.service.WeddingManagerService;
import com.kn.wedding.manager.utils.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.kn.wedding.manager.dto.CreateMessageRequest.*;

@Component
@Validated
@RequiredArgsConstructor
public class WeddingManagerHandler {
    private final WeddingManagerService managerService;
    private final ExcelService excelService;
    @Value("${wedding.date.year}")
    private Integer year;
    @Value("${wedding.date.month}")
    private Integer month;
    @Value("${wedding.date.day}")
    private Integer day;

    public void insertConfirmation(CreateConfirmationRequest request) {
        LocalDate targetDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();

        Assert.isTrue(currentDate.isBefore(targetDate), "The confirmation time is over.");

        var availableSeats = this.managerService.getGuestInformation(request.guestCode());
        Assert.isTrue(availableSeats.availableSeats() >= request.totalReservedSeats(),
                "Reserved seats exceeds available seats");

        this.managerService.insertOrUpdateConfirmation(request.toConfirmation());
    }

    public void insertGuests(CreateGuestRequest request) {
        this.managerService.insertGuets(request);
    }

    public List<ConfirmationView> getConfirmations() {
        return this.managerService.getConfirmations();
    }

    public ConfirmationView getConfirmationByGuestCode(UUID guestCode) {
        return this.managerService.getConfirmationByGuestCode(guestCode);
    }

    public List<BasicGuestView> getGuestList() {
        return this.managerService.getGuestList();
    }

    public  ResponseEntity<byte[]>  getInvitations() {
        return excelService.generateExcel(this.managerService.getInvitations(), GuestInvitationView.class, "Links de Invitados");
    }

    public GuestView getGuestByCode(UUID code) {
        return this.managerService.getGuestInformation(code);
    }

    public void insertMessage(CreateMessageRequest request) {
        request.messages().forEach(message -> this.managerService.insertOrUpdateMessage(message.toMessage()));
    }

    public ResponseEntity<byte[]> getGuestResponses() {
        return excelService.generateExcel(this.managerService.getGuestResponses(), GuestResponse.class, "Respuesta de Invitados");
    }
}

