package com.tavolo.platform.booking.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SingleTableAvailabilitySlotsGeneratedEvent extends ApplicationEvent {

    private final Long tableId;

     public SingleTableAvailabilitySlotsGeneratedEvent(Object source, Long tableId) {
        super(source);
        this.tableId = tableId;
    }
}
