package com.kn.wedding.manager.dto;

import java.util.UUID;

public record BasicGuestView(UUID code, String name, boolean hasConfirmed, String message) {
}
