package com.tavolo.platform.booking.interfaces.rest.resources;

public record TableResource(
        Long id,
        Long headquarterId,
        Integer tableNumber,
        Integer seats,
        String status,
        String zone
) {
}
