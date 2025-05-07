package com.tavolo.platform.booking.application.internal.queryservices;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.queries.GetAllBookingsQuery;
import com.tavolo.platform.booking.domain.model.queries.GetBookingByIdQuery;
import com.tavolo.platform.booking.domain.services.BookingQueryService;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingQueryServiceImpl implements BookingQueryService {
    private final BookingRepository bookingRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingQueryServiceImpl.class);

    public BookingQueryServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<Booking> handle(GetBookingByIdQuery query) {
        LOGGER.info("Searching for booking with ID: {}", query.bookingId());
        return bookingRepository.findById(query.bookingId())
                .map(
                        booking -> {
                            LOGGER.info("Booking found: {}", booking);
                            return booking;
                        }
                ).or(
                        () -> {
                            LOGGER.warn("Booking with ID {} not found", query.bookingId());
                            return Optional.empty();
                        }
                );

    }

    @Override
    public List<Booking> handle(GetAllBookingsQuery query) {
        LOGGER.info("Fetching all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        if(bookings.isEmpty()) {
            LOGGER.warn("No bookings found");
            throw new RuntimeException("No bookings found");
        }
        LOGGER.info("Found {} bookings", bookings.size());

        return bookings;
    }
}
