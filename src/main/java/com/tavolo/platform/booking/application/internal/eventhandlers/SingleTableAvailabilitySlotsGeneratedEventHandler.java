package com.tavolo.platform.booking.application.internal.eventhandlers;

import com.tavolo.platform.booking.domain.model.commands.CreateTableScheduleCommand;
import com.tavolo.platform.booking.domain.model.events.SingleTableAvailabilitySlotsGeneratedEvent;
import com.tavolo.platform.booking.domain.services.TableCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SingleTableAvailabilitySlotsGeneratedEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleTableAvailabilitySlotsGeneratedEventHandler.class);

    private final TableCommandService tableCommandService;

    public SingleTableAvailabilitySlotsGeneratedEventHandler(TableCommandService tableCommandService) {
        this.tableCommandService = tableCommandService;
    }

    @EventListener(SingleTableAvailabilitySlotsGeneratedEvent.class)
    public void on(SingleTableAvailabilitySlotsGeneratedEvent event) {
        LOGGER.info("Processing SingleTableAvailabilitySlotsGeneratedEvent for table ID: {}", event.getTableId());

        try {
            var createTableSchedulesCommand = new CreateTableScheduleCommand(event.getTableId());
            var tableOptional = tableCommandService.handle(createTableSchedulesCommand);

            if (tableOptional.isPresent()) {
                LOGGER.info("Successfully generated availability slots for table ID: {}", event.getTableId());
            } else {
                LOGGER.warn("No table returned after processing table ID: {}", event.getTableId());
            }
        } catch (Exception e) {
            LOGGER.error("Error processing availability slots for table ID: {}: {}",
                    event.getTableId(), e.getMessage(), e);
        }

    }
}
