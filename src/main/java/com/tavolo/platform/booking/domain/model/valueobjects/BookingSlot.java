package com.tavolo.platform.booking.domain.model.valueobjects;


import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull; // Para validación si se usa como DTO de entrada también

import java.time.LocalDate;
import java.util.Objects;

@Embeddable // Indica a JPA que este objeto se embebe en otra entidad (Booking)
public record BookingSlot(
        @NotNull LocalDate date, // Fecha de la reserva
        @NotNull @Enumerated(EnumType.STRING) ScheduleSlot slot // Ranura horaria
) {
    public BookingSlot { // Constructor compacto para validaciones
        Objects.requireNonNull(date);
        Objects.requireNonNull(slot);
        // Podrías añadir validación para asegurar que date no es pasada,
        // aunque lo haremos principalmente en el Command Handler/Controller
    }
}