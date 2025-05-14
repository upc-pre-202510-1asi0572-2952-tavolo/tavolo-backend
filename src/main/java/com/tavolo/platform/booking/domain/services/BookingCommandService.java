package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;
import com.tavolo.platform.booking.domain.model.commands.DeleteBookingCommand;

import java.util.Optional;

public interface BookingCommandService {
    Optional<Booking> handle(CreateBookingCommand command);
    void handle(DeleteBookingCommand command);
}
