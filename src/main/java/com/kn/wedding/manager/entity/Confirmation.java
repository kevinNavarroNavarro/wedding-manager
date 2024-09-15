package com.kn.wedding.manager.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Confirmation {
    private Integer id;
    private Integer guestId;
    private Integer reservedSeats;
    private Attend willAttend;

    @Getter
    public enum Attend {
        YES("Yes"),
        NO("No"),
        TO_CONFIRM("To Confirm");

        private final String value;

        Attend(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
