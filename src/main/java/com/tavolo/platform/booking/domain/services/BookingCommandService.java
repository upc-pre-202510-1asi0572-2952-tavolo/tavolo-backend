package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;

import java.util.Optional;

public interface BookingCommandService {
    Optional<Booking> handle(CreateBookingCommand command);
}
