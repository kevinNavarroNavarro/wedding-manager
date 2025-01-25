package com.kn.wedding.manager.controller;

import com.kn.wedding.manager.dto.*;
import com.kn.wedding.manager.handler.WeddingManagerHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Wedding Manager Controller")
@RequestMapping(value = "/wedding-manager", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeddingManagerController {
    private final WeddingManagerHandler managerHandler;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/confirmations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "create-confirmation", description = "Allow the user to create confirmation")
    public void insertConfirmation(@RequestBody CreateConfirmationRequest request) {
        this.managerHandler.insertConfirmation(request);
    }

    @GetMapping(value = "/confirmations")
    @Operation(operationId = "get-confirmations", description = "Return existing Confirmations")
    public ResponseEntity<List<ConfirmationView>> getConfirmations() {
        return ResponseEntity.ok(this.managerHandler.getConfirmations());
    }

    @GetMapping(value = "/guests/{code}/confirmations")
    @Operation(operationId = "get-confirmation-by-guest-code", description = "Return Confirmation by Guest Code")
    public ResponseEntity<ConfirmationView> getConfirmationByGuestCode(@PathVariable("code") UUID code) {
        return ResponseEntity.ok(this.managerHandler.getConfirmationByGuestCode(code));
    }

    @GetMapping(value = "/guest-list")
    @Operation(operationId = "get-guest-list", description = "Return Guests as a List")
    public ResponseEntity<List<BasicGuestView>> getGuestList() {
        return ResponseEntity.ok(this.managerHandler.getGuestList());
    }

    @GetMapping(value = "/invitations")
    @Operation(operationId = "get-invitation-by Guest", description = "Return Invitation by Guest")
    public ResponseEntity<byte[]> getInvitations() {
        return this.managerHandler.getInvitations();
    }

    @GetMapping(value = "/guests/{code}")
    @Operation(operationId = "get-guest-by-code", description = "Return Guest by Code")
    public ResponseEntity<GuestView> getGuestByCode(@PathVariable("code") UUID code) {
        return ResponseEntity.ok(this.managerHandler.getGuestByCode(code));
    }

    @GetMapping(value = "/guest-responses")
    @Operation(operationId = "get-guest-responses", description = "Return Guest Responses")
    public ResponseEntity<byte[]> getGuestResponses() {
        return this.managerHandler.getGuestResponses();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "create-message", description = "Create Message")
    public void insertMessage(@RequestBody CreateMessageRequest request) {
        this.managerHandler.insertMessage(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/guests", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "create-list-of-guest", description = "Create List of Guest")
    public void insertGuest(@RequestBody CreateGuestRequest request) {
        this.managerHandler.insertGuests(request);
    }
}