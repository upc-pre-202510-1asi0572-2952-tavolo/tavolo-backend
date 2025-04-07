package com.tavolo.platform.booking.interfaces.rest.resources;

import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para la creación de una reserva. El userId ya no es necesario aquí.
 */
public record CreateBookingResource(
        @NotNull
        @Schema(description = "ID de la mesa a reservar", example = "5")
        Long tableId,

        @NotNull
        @Schema(description = "Slot horario a reservar", example = "SLOT_1400")
        ScheduleSlot slot
) {}