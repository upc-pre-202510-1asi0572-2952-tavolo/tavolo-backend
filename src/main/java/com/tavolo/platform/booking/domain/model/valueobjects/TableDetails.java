package com.tavolo.platform.booking.domain.model.valueobjects;

import com.tavolo.platform.shared.application.exceptions.InvalidValueException;
import jakarta.persistence.Embeddable;

@Embeddable
public record TableDetails(Integer tableNumber, Integer seats) {
    public TableDetails {
        if (tableNumber < 0) {
            throw new InvalidValueException("Table number cannot be negative");
        }
        if (seats <= 0) {
            throw new InvalidValueException("Seats must be greater than zero");
        }
    }

    public TableDetails() {
        this(0, 0);
    }
}
