package com.tavolo.platform.branching.domain.model.valueobjects;

import com.tavolo.platform.shared.application.exceptions.InvalidValueException;
import jakarta.persistence.Embeddable;

import java.time.LocalTime;

@Embeddable
public record BusinessHours(
        LocalTime openingTime,
        LocalTime closingTime
) {
    public BusinessHours {
        if (openingTime == null) {
            throw new InvalidValueException("Opening time cannot be null");
        }
        if (closingTime == null) {
            throw new InvalidValueException("Closing time cannot be null");
        }
        if (openingTime.isAfter(closingTime)) {
            throw new InvalidValueException("Opening time cannot be after closing time");
        }
    }
}
