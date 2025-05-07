package com.tavolo.platform.booking.interfaces.rest.resources;

import java.time.LocalTime;

public record BookingSlotResource(
        String startTime,
        String endTime
) { }
