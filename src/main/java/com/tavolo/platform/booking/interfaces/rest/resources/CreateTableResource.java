package com.tavolo.platform.booking.interfaces.rest.resources;

public record CreateTableResource(
        Long headquarterId,
        Integer tableNumber,
        Integer seats
) {
}
