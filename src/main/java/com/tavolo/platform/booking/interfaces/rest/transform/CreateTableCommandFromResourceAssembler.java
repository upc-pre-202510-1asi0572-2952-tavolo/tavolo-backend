package com.tavolo.platform.booking.interfaces.rest.transform;

import com.tavolo.platform.booking.domain.model.commands.CreateTableCommand;
import com.tavolo.platform.booking.interfaces.rest.resources.CreateTableResource;

public class CreateTableCommandFromResourceAssembler {
    public static CreateTableCommand toCommandFromResource(CreateTableResource resource) {
        return new CreateTableCommand(
                resource.tableNumber(),
                resource.seats(),
                resource.headquarterId()
        );
    }
}
