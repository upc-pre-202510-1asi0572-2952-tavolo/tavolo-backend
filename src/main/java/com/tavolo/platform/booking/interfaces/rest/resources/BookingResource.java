package com.tavolo.platform.booking.interfaces.rest.resources;

import com.tavolo.platform.booking.domain.model.valueobjects.BookingStatus;
import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * DTO para representar una reserva creada o consultada.
 */
public record BookingResource(
        @Schema(description = "ID único de la reserva", example = "1")
        Long bookingId,

        @Schema(description = "ID del usuario que realizó la reserva", example = "101")
        Long userId,

        @Schema(description = "ID de la mesa reservada", example = "5")
        Long tableId,

        @Schema(description = "Fecha de la reserva")
        LocalDate date,

        @Schema(description = "Slot horario reservado", example = "SLOT_1400")
        ScheduleSlot slot,

        @Schema(description = "Estado actual de la reserva", example = "CONFIRMED")
        BookingStatus status
) {}