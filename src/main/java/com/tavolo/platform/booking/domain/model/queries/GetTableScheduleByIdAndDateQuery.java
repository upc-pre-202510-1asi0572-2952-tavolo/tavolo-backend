package com.tavolo.platform.booking.domain.model.queries;

import java.time.LocalDate;

public record GetTableScheduleByIdAndDateQuery(Long tableId, LocalDate date) {
}
