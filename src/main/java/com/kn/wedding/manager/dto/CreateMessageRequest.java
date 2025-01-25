package com.kn.wedding.manager.dto;

import com.kn.wedding.manager.entity.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateMessageRequest(List<MessageRequest> messages) {
    public record MessageRequest(@NotNull UUID guestCode, @NotNull String description) {
        public Message toMessage() {
            Message message = new Message();
            message.setGuestCode(guestCode);
            message.setDescription(description);

            return message;
        }
    }
}

