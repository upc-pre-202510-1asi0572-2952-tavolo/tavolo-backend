package com.tavolo.platform.booking.interfaces.rest.transform;

import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;
import com.tavolo.platform.booking.interfaces.rest.resources.CreateBookingResource;

public class CreateBookingCommandFromResourceAssembler {
    public static CreateBookingCommand toCommandFromResource(CreateBookingResource resource) {
        return new CreateBookingCommand(
                resource.clientId(),
                resource.tableId(),
                resource.bookingDate(),
                resource.slotIds()
        );
    }
}
