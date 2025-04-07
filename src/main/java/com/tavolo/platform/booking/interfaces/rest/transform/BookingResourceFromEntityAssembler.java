package com.tavolo.platform.booking.interfaces.rest.transform;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.interfaces.rest.resources.BookingResource;

/**
 * Assembler para convertir la entidad Booking a su DTO Resource.
 */
public class BookingResourceFromEntityAssembler {
    public static BookingResource toResourceFromEntity(Booking entity) {
        if (entity == null || entity.getBookingSlot() == null) {
            // Loggar advertencia si entity o bookingSlot son null inesperadamente
            return null;
        }
        return new BookingResource(
                entity.getId(),
                entity.getUserId(),
                entity.getTableId(),
                entity.getBookingSlot().date(),
                entity.getBookingSlot().slot(),
                entity.getStatus()
        );
    }
}