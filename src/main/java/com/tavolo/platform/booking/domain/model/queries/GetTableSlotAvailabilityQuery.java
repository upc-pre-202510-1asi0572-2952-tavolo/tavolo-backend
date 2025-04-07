package com.tavolo.platform.booking.domain.model.queries;


import java.time.LocalDate;

public record GetTableSlotAvailabilityQuery(Long tableId, LocalDate date) {}