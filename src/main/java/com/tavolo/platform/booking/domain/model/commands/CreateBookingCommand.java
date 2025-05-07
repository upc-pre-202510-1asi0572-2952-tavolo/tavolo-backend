package com.tavolo.platform.booking.domain.model.commands;

import java.time.LocalDate;
import java.util.List;

public record CreateBookingCommand(
        Long clientId,
        Long tableId,
        LocalDate bookingDate,
        List<Long> slotIds
) {
}
