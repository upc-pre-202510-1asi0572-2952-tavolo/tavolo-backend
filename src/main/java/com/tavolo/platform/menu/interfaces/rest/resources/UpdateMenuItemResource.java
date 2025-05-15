package com.tavolo.platform.menu.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Recurso para la actualización de un ítem del menú existente.
 */
public record UpdateMenuItemResource(
        @Schema(description = "Nombre del ítem del menú", example = "Ceviche Mixto")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String name,

        @Schema(description = "Descripción del ítem", example = "Delicioso ceviche mixto con pescado, calamares y camarones")
        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
        String description,

        @Schema(description = "Precio del ítem", example = "29.90")
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
        @Digits(integer = 6, fraction = 2, message = "El precio debe tener máximo 6 dígitos enteros y 2 decimales")
        BigDecimal price,

        @Schema(description = "Categoría del ítem", example = "ENTRADAS")
        @NotNull(message = "La categoría es obligatoria")
        String category,

        @Schema(description = "Imagen en formato Base64", example = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQE...")
        @NotBlank(message = "La imagen es obligatoria")
        String imageBase64
) {
} 