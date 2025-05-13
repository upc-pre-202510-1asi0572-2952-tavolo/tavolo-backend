package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.queries.GetAllBookingsByIdClientQuery;
import com.tavolo.platform.booking.domain.model.queries.GetAllBookingsQuery;
import com.tavolo.platform.booking.domain.model.queries.GetBookingByIdQuery;

import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    Optional<Booking> handle(GetBookingByIdQuery query);
    List<Booking> handle(GetAllBookingsQuery query);
    List<Booking> handle(GetAllBookingsByIdClientQuery query);
}
