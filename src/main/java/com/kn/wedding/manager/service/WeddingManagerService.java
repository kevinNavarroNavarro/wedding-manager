package com.kn.wedding.manager.service;

import com.kn.wedding.manager.dto.*;
import com.kn.wedding.manager.entity.Confirmation;
import com.kn.wedding.manager.entity.Guest;
import com.kn.wedding.manager.entity.Message;
import com.kn.wedding.manager.exception.RecordNotFoundException;
import com.kn.wedding.manager.repository.ConfirmationRepository;
import com.kn.wedding.manager.repository.GuestRepository;
import com.kn.wedding.manager.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeddingManagerService {
    private final GuestRepository guestRepository;
    private final MessageRepository messageRepository;
    private final ConfirmationRepository confirmationRepository;

    public void insertOrUpdateConfirmation(Confirmation confirmation) {
        if (existsConfirmation(confirmation.getGuestCode())) {
            updateConfirmation(confirmation);
            return;
        }

        insertConfirmation(confirmation);
    }

    public List<ConfirmationView> getConfirmations() {
        return confirmationRepository.findAll().stream()
                .map(c -> {
                    Guest guest = this.guestRepository.findByCode(c.getGuestCode())
                            .orElseThrow(() -> new RuntimeException("Guest not found"));
                    return new ConfirmationView(c.getId(), c.getGuestCode(), guest.getName(), c.getReservedSeats(), c.getWillAttend());
                }).collect(Collectors.toList());
    }

    public ConfirmationView getConfirmationByGuestCode(UUID guestCode) {
        Confirmation confirmation = this.confirmationRepository.findByGuestCode(guestCode)
                .orElseThrow(() -> new RecordNotFoundException("Confirmation not found with code: " + guestCode));

        Guest guest = this.guestRepository.findByCode(guestCode)
                .orElseThrow(() -> new RecordNotFoundException("Guest not found with code: " + guestCode));

        return new ConfirmationView(
                confirmation.getId(),
                confirmation.getGuestCode(),
                guest.getName(),
                confirmation.getReservedSeats(),
                confirmation.getWillAttend()
        );
    }

    public List<BasicGuestView> getGuestList() {
        return this.guestRepository.findAll().stream()
                .map(g -> {
                    Confirmation confirmation = this.confirmationRepository.findByGuestCode(g.getCode()).orElse(null);
                    Message message = this.messageRepository.findByGuestCode(g.getCode()).orElse(null);

                    return new BasicGuestView(g.getCode(), g.getName(),confirmation != null, message != null ? message.getDescription() : null);
                }).collect(Collectors.toList());
    }

    public List<GuestInvitationView> getInvitations() {
        return this.guestRepository.findAll().stream()
                .map(g -> GuestInvitationView.generateInvitation(g.getName(), g.getCode())).collect(Collectors.toList());
    }

    public GuestView getGuestInformation(UUID guestCode) {
        Guest guest = this.guestRepository.findByCode(guestCode)
                .orElseThrow(() -> new RecordNotFoundException("Guest not found with code: " + guestCode));

        Confirmation confirmation = this.confirmationRepository.findByGuestCode(guestCode).orElse(null);
        Message message = this.messageRepository.findByGuestCode(guestCode).orElse(null);

        return new GuestView(guest.getCode(), guest.getName(), guest.getAvailableSeats(), confirmation != null,
                message != null ? message.getDescription() : null);
    }

    public void insertOrUpdateMessage(Message message) {
        this.messageRepository.findByGuestCode(message.getGuestCode()).ifPresentOrElse(
                existingMessage -> {
                    existingMessage.setDescription(message.getDescription());
                    this.messageRepository.save(existingMessage);
                }, () -> this.messageRepository.save(message)
        );
    }

    public void insertGuets(CreateGuestRequest request) {
        List<Guest> toCreate = request.guests().stream().map(guest -> guest.toGuest(generateUniqueGuestCode())).toList();
        toCreate.forEach(guest -> this.guestRepository.save(guest));
    }

    private UUID generateUniqueGuestCode() {
        UUID code;
        do {
            code = UUID.randomUUID();
        } while (this.guestRepository.existsByCode(code));

        return code;
    }

    public List<GuestResponse> getGuestResponses() {
        return this.confirmationRepository.findAll().stream()
                .filter(c -> c.getConfirmedAt() != null)
                .map(c -> {
                    Guest guest = this.guestRepository.findByCode(c.getGuestCode()).orElseThrow(() -> new RuntimeException("Guest not found"));
                    return new GuestResponse(guest.getName(), c.getConfirmedAt(), c.getReservedSeats(), c.getWillAttend());
                }).collect(Collectors.toList());
    }

    private void insertConfirmation(Confirmation confirmation) {
        confirmation.setConfirmedAt(new Date());
        this.confirmationRepository.save(confirmation);
    }

    private void updateConfirmation(Confirmation confirmation) {
        Confirmation existing = this.confirmationRepository.findByGuestCode(confirmation.getGuestCode())
                .orElseThrow(() -> new RecordNotFoundException("Confirmation not found"));
        existing.setReservedSeats(confirmation.getReservedSeats());
        existing.setWillAttend(confirmation.getWillAttend());

        this.confirmationRepository.save(existing);
    }

    public boolean existsConfirmation(UUID guestCode) {
        return confirmationRepository.findByGuestCode(guestCode).isPresent();
    }
}