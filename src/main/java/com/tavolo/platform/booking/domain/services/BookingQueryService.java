package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.aggregates.Table; // Necesario para el tipo de retorno
import com.tavolo.platform.booking.domain.model.queries.GetTableSlotAvailabilityQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTablesInHeadquarterQuery;
import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot; // Necesario para el tipo de retorno

import java.util.List;
import java.util.Map;

/**
 * Interface defining the query handling services for the Booking context.
 * Defines the contract for booking query operations.
 */
public interface BookingQueryService {

    /**
     * Handles the GetTablesInHeadquarterQuery.
     * @param query The query containing the headquarter ID.
     * @return A list of Table aggregates belonging to the specified headquarter.
     */
    List<Table> handle(GetTablesInHeadquarterQuery query);

    /**
     * Handles the GetTableSlotAvailabilityQuery.
     * @param query The query containing the table ID and date (always today).
     * @return A map where the key is the ScheduleSlot and the value is the status ("AVAILABLE" or "RESERVED").
     */
    Map<ScheduleSlot, String> handle(GetTableSlotAvailabilityQuery query);

    // Aquí añadirías las firmas para otros queries de Booking si los tuvieras
    // Optional<Booking> handle(GetBookingByIdQuery query);

}