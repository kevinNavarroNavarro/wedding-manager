package com.kn.wedding.manager.handler;

import com.kn.wedding.manager.WeddingManagerDatabaseInitializer;
import com.kn.wedding.manager.dto.CreateConfirmationRequest;
import com.kn.wedding.manager.dto.CreateMessageRequest;
import com.kn.wedding.manager.entity.Guest;
import com.kn.wedding.manager.repository.GuestRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class WeddingManagerHandlerTest extends WeddingManagerDatabaseInitializer {
    private @Autowired WeddingManagerHandler managerHandler;

    @Autowired
    private GuestRepository guestRepository;

    @BeforeEach
    public void setUp() {
        createMockData();
    }

    private void createMockData() {
        Guest guest1 = new Guest(new ObjectId(), "Example 1", 5, UUID.randomUUID());
        Guest guest2 = new Guest(new ObjectId(), "Example 2", 3, UUID.randomUUID());
        guestRepository.insert(List.of(guest1, guest2));
    }

    @Test
    void getGuestAsListAndExpectSuccessResultExceedsSeats() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());

        Assertions.assertNotNull(guests);
        Assertions.assertNotEquals(0, guests.size());
        Assertions.assertTrue(guests.stream().allMatch(guest -> Objects.nonNull(guest.name())));
        Assertions.assertTrue(guests.stream().allMatch(guest -> Objects.nonNull(guest.code())));
    }

    @Test
    void getGuestByCodeAndExpectSuccessResultExceedsSeats() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());

        Assertions.assertNotNull(guests);
        Assertions.assertNotEquals(0, guests.size());

        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        var guestView = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestByCode(guest.code()));
        Assertions.assertNotNull(guestView);
        Assertions.assertEquals(guest.code(), guestView.code());
        Assertions.assertEquals(guest.name(), guestView.name());
    }

    @Test
    void insertConfirmationAndExpectErrorResultExceedsSeats() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        CreateConfirmationRequest confirmation = new CreateConfirmationRequest(guest.code(), 10, "To Confirm");

        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> this.managerHandler.insertConfirmation(confirmation));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Reserved seats exceeds available seats", exception.getMessage());
    }

    @Test
    void insertConfirmationAndExpectSuccessResult() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        CreateConfirmationRequest confirmation = new CreateConfirmationRequest(guest.code(), 2, "Yes");

        Assertions.assertDoesNotThrow(() -> this.managerHandler.insertConfirmation(confirmation));
    }

    @Test
    void getGuestResponsesAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        CreateConfirmationRequest confirmation = new CreateConfirmationRequest(guest.code(), 3, "Yes");

        Assertions.assertDoesNotThrow(() -> this.managerHandler.insertConfirmation(confirmation));

        var guestResponses = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestResponses());
        Assertions.assertNotNull(guestResponses);
        Assertions.assertNotNull(guestResponses.getBody());
        Assertions.assertEquals(HttpStatus.OK, guestResponses.getStatusCode());
    }

    @Test
    void insertMessageAndExpectSuccessResultAssertContent() {
        var guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());
        var guest = guests.stream().findFirst().orElseThrow(() -> new IllegalStateException("At least one record must be provided"));

        List<CreateMessageRequest.MessageRequest> messages = List.of(new CreateMessageRequest.MessageRequest(guest.code(), "Hello There!"));
        CreateMessageRequest request = new CreateMessageRequest(messages);

        Assertions.assertDoesNotThrow(() -> this.managerHandler.insertMessage(request));

        guests = Assertions.assertDoesNotThrow(() -> this.managerHandler.getGuestList());
        Assertions.assertNotNull(guests);
        Assertions.assertNotEquals(0, guests.size());
        Assertions.assertTrue(guests.stream()
                .filter(item -> item.code().equals(guest.code()))
                .allMatch(item -> item.message().equals("Hello There!")));
    }
}