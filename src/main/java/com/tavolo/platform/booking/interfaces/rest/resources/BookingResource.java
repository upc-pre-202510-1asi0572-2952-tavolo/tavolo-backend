package com.tavolo.platform.booking.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.List;

public record BookingResource(
        Long id,
        Long clientId,
        Long tableNumber,
        Long headquarterId,
        Long tableId,
        LocalDate bookingDate,
        List<BookingSlotResource> bookingSlots
) {}
