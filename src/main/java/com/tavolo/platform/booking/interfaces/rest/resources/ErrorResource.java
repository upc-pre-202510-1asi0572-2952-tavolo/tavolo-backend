package com.tavolo.platform.booking.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO estándar para devolver mensajes de error en la API.
 */
public record ErrorResource(
        @Schema(description = "Mensaje descriptivo del error", example = "Slot already booked")
        String message
) {}