package com.tavolo.platform.branching.domain.model.valueobjects;

import com.tavolo.platform.shared.application.exceptions.InvalidValueException;
import jakarta.persistence.Embeddable;

@Embeddable
public record Coordinates(
        Double latitude,
        Double longitude
) {
    public Coordinates {
        if (latitude == null ) {
            throw new InvalidValueException("Latitude cannot be null");
        }
        if (longitude == null ) {
            throw new InvalidValueException("Longitude cannot be null");
        }
    }
}
