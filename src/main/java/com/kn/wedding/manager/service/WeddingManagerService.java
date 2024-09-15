package com.kn.wedding.manager.service;

import com.kn.wedding.manager.entity.Confirmation;
import com.kn.wedding.manager.dto.ConfirmationView;
import com.kn.wedding.manager.entity.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeddingManagerService {
    private final JdbcClient jdbcClient;

    public void insertOrUpdateConfirmation(Confirmation confirmation) {
        if (!existsConfirmation(confirmation.getGuestId())) {
            insertConfirmation(confirmation);
            return;
        }

        updateConfirmation(confirmation);
    }

    private void insertConfirmation(Confirmation confirmation) {
        this.jdbcClient
                .sql("""
                        INSERT INTO confirmations (guest_id, confirmed_at, reserved_seat, will_attend)
                        VALUES (:guest_id, CURRENT_TIMESTAMP, :reserved_seat, :will_attend);
                        """)
                .param("guest_id", confirmation.getGuestId())
                .param("reserved_seat", confirmation.getReservedSeats())
                .param("will_attend", confirmation.getWillAttend().getValue())
                .update();
    }

    private void updateConfirmation(Confirmation confirmation) {
        this.jdbcClient
                .sql("""
                        UPDATE confirmations
                        SET reserved_seat = :reserved_seat, will_attend = :will_attend
                        WHERE guest_id = :guest_id;
                        """)
                .param("guest_id", confirmation.getGuestId())
                .param("reserved_seat", confirmation.getReservedSeats())
                .param("will_attend", confirmation.getWillAttend().getValue())
                .update();
    }

    private boolean existsConfirmation(Integer guestId) {
        var exists = this.jdbcClient
                .sql("""
                        SELECT COUNT(*) FROM confirmations WHERE guest_id = :guest_id
                        """)
                .param("guest_id", guestId)
                .query(Integer.class)
                .optional().orElse(0);

        return exists > 0;
    }

    public List<ConfirmationView> getConfirmations() {
        return jdbcClient
                .sql("""
                        SELECT c.id, c.guest_id, g.name AS guest_name, c.reserved_seat AS total_reserved_seats, c.will_attend
                        FROM confirmations c
                            JOIN guests g ON g.id = c.guest_id
                        """)
                .query(ConfirmationView.class)
                .list();
    }

    public Guest getGuestInformation(Integer guestId) {
        return jdbcClient
                .sql("SELECT id, name, available_seat AS available_seats FROM guests WHERE id = :id")
                .param("id", guestId)
                .query(Guest.class)
                .single();
    }
}
