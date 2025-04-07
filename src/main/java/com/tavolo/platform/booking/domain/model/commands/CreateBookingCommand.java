package com.tavolo.platform.booking.domain.model.commands;

import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot;
import java.time.LocalDate;

// Comando para solicitar la creación de una reserva (AHORA CON userId como primer parámetro)
public record CreateBookingCommand(
        Long userId,
        Long tableId,
        LocalDate date,       // Fecha de la reserva (validada como hoy)
        ScheduleSlot slot      // Slot deseado
) {}