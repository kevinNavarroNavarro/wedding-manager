package com.kn.wedding.manager.dto;

import java.util.UUID;

public record GuestInvitationView(String name, String url) {
    public static GuestInvitationView generateInvitation(String name, UUID code) {
        final String URL= "https://boda-mk.com/?guestCode=";
        return new GuestInvitationView(name, "%s%s".formatted(URL, code));
    }
}
