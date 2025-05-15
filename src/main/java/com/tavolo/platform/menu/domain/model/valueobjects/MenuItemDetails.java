package com.tavolo.platform.menu.domain.model.valueobjects;

import com.tavolo.platform.shared.application.exceptions.InvalidValueException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;

import java.math.BigDecimal;

/**
 * Value object que encapsula los detalles de un ítem del menú.
 */
@Embeddable
public record MenuItemDetails(
        String name,
        String description,
        BigDecimal price,
        @Lob
        String imageBase64
) {
    public MenuItemDetails {
        if (name == null || name.isBlank()) {
            throw new InvalidValueException("El nombre no puede estar vacío");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException("El precio debe ser mayor a cero");
        }
    }
} 