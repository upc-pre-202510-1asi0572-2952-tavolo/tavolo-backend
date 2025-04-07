package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;
// Importar otros commands de Booking si los hubiera (ej: CancelBookingCommand)

/**
 * Interface defining the command handling services for the Booking context.
 * Defines the contract for booking command operations.
 */
public interface BookingCommandService {

    /**
     * Handles the CreateBookingCommand.
     * @param command The command containing booking creation details.
     * @return The ID of the newly created Booking aggregate.
     */
    Long handle(CreateBookingCommand command);

    // Aquí añadirías las firmas para otros comandos de Booking si los tuvieras
    // void handle(CancelBookingCommand command);
    // ...
}