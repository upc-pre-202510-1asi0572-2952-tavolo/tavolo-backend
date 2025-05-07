package com.tavolo.platform.booking.interfaces.rest.transform;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.interfaces.rest.resources.BookingResource;
import com.tavolo.platform.booking.interfaces.rest.resources.BookingSlotResource;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BookingResourceFromEntityAssembler {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static BookingResource toResourceFromEntity(Booking booking) {
        List<BookingSlotResource> slotResources = booking.getBookingSlots().stream()
                .map(slot -> new BookingSlotResource(
                        slot.getTimeInterval().startTime().format(TIME_FORMATTER),
                        slot.getTimeInterval().endTime().format(TIME_FORMATTER)))
                .collect(Collectors.toList());
        return new BookingResource(
                booking.getId(),
                booking.getUserId().clientId(),   // assuming UserId has a getValue() method
                booking.getTableId().getId(),       // assuming Table has a getId() method
                booking.getBookingDate(),
                slotResources
        );
    }
}
