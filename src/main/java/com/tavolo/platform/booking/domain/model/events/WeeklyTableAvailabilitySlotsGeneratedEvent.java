package com.tavolo.platform.booking.domain.model.events;

import com.tavolo.platform.booking.domain.model.valueobjects.HeadquarterId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalTime;

@Getter
public class WeeklyTableAvailabilitySlotsGeneratedEvent extends ApplicationEvent {

    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final Integer intervalMinutes;
    private final HeadquarterId headquarterId;

    public WeeklyTableAvailabilitySlotsGeneratedEvent(Object source, LocalTime openingTime, LocalTime closingTime, Integer intervalMinutes, HeadquarterId headquarterId) {
        super(source);
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.intervalMinutes = intervalMinutes;
        this.headquarterId = headquarterId;
    }
}
