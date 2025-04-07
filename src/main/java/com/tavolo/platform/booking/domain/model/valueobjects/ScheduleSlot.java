package com.tavolo.platform.booking.domain.model.valueobjects;

import lombok.Getter;

@Getter
public enum ScheduleSlot {
    SLOT_0600 ("6:00 AM - 7:00 AM"),
    SLOT_0700 ("7:00 AM - 8:00 AM"),
    SLOT_0800 ("8:00 AM - 9:00 AM"),
    SLOT_0900 ("9:00 AM - 10:00 AM"),
    SLOT_1000 ("10:00 AM - 11:00 AM"),
    SLOT_1100 ("11:00 AM - 12:00 PM"),
    SLOT_1200 ("12:00 PM - 1:00 PM"),
    SLOT_1300 ("1:00 PM - 2:00 PM"),
    SLOT_1400 ("2:00 PM - 3:00 PM"),
    SLOT_1500 ("3:00 PM - 4:00 PM"),
    SLOT_1600 ("4:00 PM - 5:00 PM"),
    SLOT_1700 ("5:00 PM - 6:00 PM"),
    SLOT_1800 ("6:00 PM - 7:00 PM"),
    SLOT_1900 ("7:00 PM - 8:00 PM"),
    SLOT_2000 ("8:00 PM - 9:00 PM");
    // Ajustar si el horario real es diferente

    private final String description;

    ScheduleSlot(String description) {
        this.description = description;
    }

}