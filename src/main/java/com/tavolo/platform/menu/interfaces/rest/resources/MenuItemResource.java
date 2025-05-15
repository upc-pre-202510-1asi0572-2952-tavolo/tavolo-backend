package com.tavolo.platform.menu.interfaces.rest.resources;

import java.math.BigDecimal;

/**
 * Recurso que representa un ítem del menú en la API.
 */
public record MenuItemResource(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        String imageBase64
) {
} 