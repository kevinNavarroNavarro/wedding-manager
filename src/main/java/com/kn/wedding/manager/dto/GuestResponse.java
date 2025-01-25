package com.kn.wedding.manager.dto;

import java.util.Date;

public record GuestResponse(String name, Date confirmedAt, Integer reservedSeat, String willAttend) {
}