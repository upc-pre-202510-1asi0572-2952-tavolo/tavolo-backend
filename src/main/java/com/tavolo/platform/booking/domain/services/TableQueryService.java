package com.tavolo.platform.booking.domain.services;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.entities.AvailabilitySlot;
import com.tavolo.platform.booking.domain.model.queries.GetAllTableByHeadquarterIdQuery;
import com.tavolo.platform.booking.domain.model.queries.GetAllTablesQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTableByIdQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTableScheduleByIdAndDateQuery;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TableQueryService {
    Optional<Table> handle(GetTableByIdQuery query);
    Set<Table> handle(GetAllTablesQuery query);
    List<AvailabilitySlot> handle(GetTableScheduleByIdAndDateQuery query);
    List<Table> handle(GetAllTableByHeadquarterIdQuery query);
}
