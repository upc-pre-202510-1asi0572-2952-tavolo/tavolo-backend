package com.tavolo.platform.booking.domain.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.CONFLICT) // Devuelve 409 Conflict si se lanza
public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(Long tableId, String slot, LocalDate date) {
        super("Table " + tableId + " is already booked for slot " + slot + " on " + date);
    }
}