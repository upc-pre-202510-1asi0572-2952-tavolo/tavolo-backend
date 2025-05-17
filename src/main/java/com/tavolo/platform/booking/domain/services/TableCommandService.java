package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.commands.CreateTableCommand;
import com.tavolo.platform.booking.domain.model.commands.CreateTableScheduleCommand;
import com.tavolo.platform.booking.domain.model.commands.DeleteTableCommand;

import java.util.Optional;

public interface TableCommandService {
    Optional<Table> handle(CreateTableCommand command);
    Optional<Table> handle(CreateTableScheduleCommand command);
    void handle(DeleteTableCommand command);
}
