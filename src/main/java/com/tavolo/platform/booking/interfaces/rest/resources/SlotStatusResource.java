package com.tavolo.platform.booking.interfaces.rest.resources;

import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para representar el estado de un slot horario en la API.
 */
public record SlotStatusResource(
        @Schema(description = "Identificador del slot horario", example = "SLOT_0900")
        ScheduleSlot slot,

        @Schema(description = "Descripción legible del slot", example = "9:00 AM - 10:00 AM")
        String description,

        @Schema(description = "Estado de disponibilidad del slot", example = "AVAILABLE", allowableValues = {"AVAILABLE", "RESERVED"})
        String status
) {}