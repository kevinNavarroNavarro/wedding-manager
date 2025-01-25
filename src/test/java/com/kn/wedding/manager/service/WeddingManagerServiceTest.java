package com.kn.wedding.manager.service;

import com.kn.wedding.manager.WeddingManagerDatabaseInitializer;
import com.kn.wedding.manager.entity.Confirmation;
import com.kn.wedding.manager.entity.Guest;
import com.kn.wedding.manager.entity.Message;
import com.kn.wedding.manager.repository.GuestRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class WeddingManagerServiceTest extends WeddingManagerDatabaseInitializer {
    private @Autowired WeddingManagerService managerService;

    @Autowired
    private GuestRepository guestRepository;

    @BeforeEach
    public void setUp() {
        createMockData();
    }

    private void createMockData() {
        Guest guest1 = new Guest(new ObjectId(), "Example 1", 10, UUID.randomUUID());
        Guest guest2 = new Guest(new ObjectId(), "Example 2", 3, UUID.randomUUID());
        guestRepository.insert(List.of(guest1, guest2));
    }

    @Test
    void insertConfirmationAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        Confirmation confirmation = new Confirmation();
        confirmation.setGuestCode(guest.code());
        confirmation.setReservedSeats(3);
        confirmation.setWillAttend("Yes");

        Assertions.assertDoesNotThrow(() -> this.managerService.insertOrUpdateConfirmation(confirmation));

        var confirmations = Assertions.assertDoesNotThrow(() -> this.managerService.getConfirmations());
        Assertions.assertNotNull(confirmations);
        Assertions.assertNotEquals(0, confirmations.size());
    }

    @Test
    void insertAndUpdateConfirmationAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        // Insert record
        Confirmation confirmation = new Confirmation();
        confirmation.setReservedSeats(3);
        confirmation.setWillAttend("Yes");
        confirmation.setGuestCode(guest.code());

        Assertions.assertDoesNotThrow(() -> this.managerService.insertOrUpdateConfirmation(confirmation));
        var confirmations = Assertions.assertDoesNotThrow(() -> this.managerService.getConfirmations());

        Assertions.assertNotNull(confirmations);
        Assertions.assertNotEquals(0, confirmations.size());
        Assertions.assertTrue(confirmations.stream().allMatch(item -> confirmation.getReservedSeats().equals(item.totalReservedSeats())));
        Assertions.assertTrue(confirmations.stream().allMatch(item -> confirmation.getWillAttend().equals(item.willAttend())));

        // Update existing record
        confirmation.setReservedSeats(5);
        confirmation.setGuestCode(guest.code());
        confirmation.setWillAttend("To Confirm");

        Assertions.assertDoesNotThrow(() -> this.managerService.insertOrUpdateConfirmation(confirmation));
        confirmations = Assertions.assertDoesNotThrow(() -> this.managerService.getConfirmations());

        Assertions.assertNotNull(confirmations);
        Assertions.assertNotEquals(0, confirmations.size());
        Assertions.assertTrue(confirmations.stream().allMatch(item -> confirmation.getReservedSeats().equals(item.totalReservedSeats())));
        Assertions.assertTrue(confirmations.stream().allMatch(item -> confirmation.getWillAttend().equals(item.willAttend())));
    }

    @Test
    void getConfirmationByCodeAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        Confirmation confirmation = new Confirmation();
        confirmation.setGuestCode(guest.code());
        confirmation.setReservedSeats(3);
        confirmation.setWillAttend("Yes");

        Assertions.assertDoesNotThrow(() -> this.managerService.insertOrUpdateConfirmation(confirmation));

        var confirmationView = Assertions.assertDoesNotThrow(() -> this.managerService.getConfirmationByGuestCode(guest.code()));
        Assertions.assertEquals(confirmation.getGuestCode(), confirmationView.guestCode());
        Assertions.assertEquals(confirmation.getReservedSeats(), confirmationView.totalReservedSeats());
        Assertions.assertEquals(confirmation.getWillAttend(), confirmationView.willAttend());
    }

    @Test
    void getGuestResponsesAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        Confirmation confirmation = new Confirmation();
        confirmation.setGuestCode(guest.code());
        confirmation.setReservedSeats(3);
        confirmation.setWillAttend("Yes");

        Assertions.assertDoesNotThrow(() -> this.managerService.insertOrUpdateConfirmation(confirmation));

        var guestResponses = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestResponses());
        Assertions.assertNotNull(guestResponses);
        Assertions.assertNotEquals(0, guestResponses.size());
    }

    @Test
    void insertMessageAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        Message message = new Message();
        message.setGuestCode(guest.code());
        message.setDescription("Hello there!");

        Assertions.assertDoesNotThrow(() -> this.managerService.insertOrUpdateMessage(message));

        guests = Assertions.assertDoesNotThrow(() -> this.managerService.getGuestList());
        Assertions.assertNotNull(guests);
        Assertions.assertNotEquals(0, guests.size());
        Assertions.assertTrue(guests.stream()
                .filter(item -> item.code().equals(guest.code()))
                .allMatch(item -> item.message().equals(message.getDescription())));
    }
}