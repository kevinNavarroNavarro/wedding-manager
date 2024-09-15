package com.kn.wedding.manager.service;

import com.kn.wedding.manager.WeddingManagerDatabaseInitializer;
import com.kn.wedding.manager.entity.Confirmation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.kn.wedding.manager.entity.Confirmation.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {WeddingManagerDatabaseInitializer.class, WeddingManagerService.class})
class WeddingManagerServiceTest {
    private @Autowired WeddingManagerService managerService;

    @Test
    @Transactional
    void insertConfirmationAndExpectSuccessResultAssertContent() {
         Confirmation confirmation = new Confirmation();
         confirmation.setGuestId(1);
         confirmation.setReservedSeats(3);
         confirmation.setWillAttend(Attend.YES);

        Assertions.assertDoesNotThrow(()-> this.managerService.insertOrUpdateConfirmation(confirmation));

        var confirmations = Assertions.assertDoesNotThrow(()-> this.managerService.getConfirmations());
        Assertions.assertNotNull(confirmations);
        Assertions.assertNotEquals(0, confirmations.size());
    }

    @Test
    @Transactional
    void insertAndUpdateConfirmationAndExpectSuccessResultAssertContent() {
        // Insert record
        Confirmation confirmationEntity = new Confirmation();
        confirmationEntity.setGuestId(1);
        confirmationEntity.setReservedSeats(3);
        confirmationEntity.setWillAttend(Attend.YES);

        Assertions.assertDoesNotThrow(()-> this.managerService.insertOrUpdateConfirmation(confirmationEntity));
        var confirmations = Assertions.assertDoesNotThrow(()-> this.managerService.getConfirmations());

        Assertions.assertNotNull(confirmations);
        Assertions.assertNotEquals(0, confirmations.size());
        Assertions.assertTrue(confirmations.stream().allMatch(confirmation -> confirmationEntity.getReservedSeats().equals(confirmation.totalReservedSeats())));
        Assertions.assertTrue(confirmations.stream().allMatch(confirmation -> confirmationEntity.getWillAttend().getValue().equals(confirmation.willAttend())));

        // Update existing record
        confirmationEntity.setGuestId(1);
        confirmationEntity.setReservedSeats(5);
        confirmationEntity.setWillAttend(Attend.TO_CONFIRM);

        Assertions.assertDoesNotThrow(()-> this.managerService.insertOrUpdateConfirmation(confirmationEntity));
        confirmations = Assertions.assertDoesNotThrow(()-> this.managerService.getConfirmations());

        Assertions.assertNotNull(confirmations);
        Assertions.assertNotEquals(0, confirmations.size());
        Assertions.assertTrue(confirmations.stream().allMatch(confirmation -> confirmationEntity.getReservedSeats().equals(confirmation.totalReservedSeats())));
        Assertions.assertTrue(confirmations.stream().allMatch(confirmation -> confirmationEntity.getWillAttend().getValue().equals(confirmation.willAttend())));
    }
}