package com.kn.wedding.manager.handler;

import com.kn.wedding.manager.WeddingManagerDatabaseInitializer;
import com.kn.wedding.manager.dto.CreateConfirmationRequest;
import com.kn.wedding.manager.service.WeddingManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.kn.wedding.manager.entity.Confirmation.Attend;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {WeddingManagerDatabaseInitializer.class,  WeddingManagerHandler.class, WeddingManagerService.class})
class WeddingManagerHandlerTest {
    private @Autowired WeddingManagerHandler mangerHandler;

    @Test
    void insertConfirmationAndExpectErrorResultExceedsSeats() {
         CreateConfirmationRequest confirmation = new CreateConfirmationRequest(1, 10, Attend.TO_CONFIRM);

        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> this.mangerHandler.insertConfirmation(confirmation));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Reserved seats exceeds available seats", exception.getMessage());
    }

    @Test
    @Transactional
    void insertConfirmationAndExpectSuccessResult() {
        CreateConfirmationRequest confirmation = new CreateConfirmationRequest(2, 2, Attend.YES);

        Assertions.assertDoesNotThrow(() -> this.mangerHandler.insertConfirmation(confirmation));
    }
}