package com.tavolo.platform.booking.interfaces.rest.resources;


/**
 * DTO para representar una mesa básica en la API.
 * Utilizado para listar mesas disponibles para seleccionar.
 */
public record TableResource(
        Long id,
        String identifier,
        int capacity
) {}