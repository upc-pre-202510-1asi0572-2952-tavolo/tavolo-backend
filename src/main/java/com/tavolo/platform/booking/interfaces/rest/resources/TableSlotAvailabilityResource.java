package com.tavolo.platform.booking.interfaces.rest.resources;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para la respuesta de disponibilidad de slots de una mesa para hoy.
 */
public record TableSlotAvailabilityResource(
        @Schema(description = "ID de la mesa consultada")
        Long tableId,

        @Schema(description = "Fecha para la cual se muestra la disponibilidad (siempre hoy)")
        LocalDate date,

        @Schema(description = "Lista ordenada de slots horarios y su estado de disponibilidad")
        List<SlotStatusResource> slots
) {}