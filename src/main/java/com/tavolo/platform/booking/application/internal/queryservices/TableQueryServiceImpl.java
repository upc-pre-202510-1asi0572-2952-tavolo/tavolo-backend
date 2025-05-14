package com.tavolo.platform.booking.application.internal.queryservices;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.entities.AvailabilitySlot;
import com.tavolo.platform.booking.domain.model.queries.GetAllTableByHeadquarterIdQuery;
import com.tavolo.platform.booking.domain.model.queries.GetAllTablesQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTableByIdQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTableScheduleByIdAndDateQuery;
import com.tavolo.platform.booking.domain.services.TableQueryService;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.TableRepository;
import com.tavolo.platform.shared.application.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableQueryServiceImpl implements TableQueryService {

    private final TableRepository tableRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TableQueryServiceImpl.class);

    public TableQueryServiceImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public Optional<Table> handle(GetTableByIdQuery query) {
        LOGGER.info("Searching for table with ID: {}", query.id());
        return tableRepository.findById(query.id())
                .map(table -> {
                    LOGGER.info("Table found with ID {}: {}", query.id(), table);
                    return table;
                })
                .or(() -> {
                    LOGGER.warn("No table found with ID: {}", query.id());
                    throw new ResourceNotFoundException("No table found with ID: " + query.id());
                });
    }

    @Override
    public Set<Table> handle(GetAllTablesQuery query) {
        LOGGER.info("Starting query for all tables");
        var tables = tableRepository.findAll();

        if (tables.isEmpty()) {
            LOGGER.warn("No tables found in the database");
            throw new ResourceNotFoundException("No tables found");
        }

        LOGGER.info("Query successful: {} tables found", tables.size());
        return new HashSet<>(tables);
    }

    @Override
    public List<AvailabilitySlot> handle(GetTableScheduleByIdAndDateQuery query) {
        LOGGER.info("Searching for availability slots for table ID: {} on date: {}", query.tableId(), query.date());

        // Check if the table exists
        if (!tableRepository.existsById(query.tableId())) {
            LOGGER.warn("No table found with ID: {}", query.tableId());
            throw new ResourceNotFoundException("No table found with ID: " + query.tableId());
        }

        // Get availability slots
        List<AvailabilitySlot> availabilitySlots = tableRepository.findAvailabilitySlotsByTableIdAndDate(query.tableId(), query.date());

        // Ordenar los slots por startTime antes de retornar
        availabilitySlots.sort(Comparator.comparing(slot -> slot.getTimeInterval().startTime()));

        if (availabilitySlots.isEmpty()) {
            LOGGER.info("No availability slots found for table ID: {} on date: {}", query.tableId(), query.date());
        } else {
            LOGGER.info("Found {} availability slots for table ID: {} on date: {}",
                    availabilitySlots.size(), query.tableId(), query.date());
        }

        return availabilitySlots;
    }

    @Override
    public List<Table> handle(GetAllTableByHeadquarterIdQuery query) {
        LOGGER.info("Searching for tables associated with headquarter ID: {}", query.headquarterId());

        List<Table> allTables = tableRepository.findAll();
        List<Table> filteredTables = allTables.stream()
                .filter(table -> table.getHeadquarterId().headquarterId().equals(query.headquarterId()))
                .collect(Collectors.toList());

        if (filteredTables.isEmpty()) {
            LOGGER.warn("No tables found for headquarter with ID: {}", query.headquarterId());
            throw new ResourceNotFoundException("No tables found for headquarter with ID: " + query.headquarterId());
        }

        LOGGER.info("Found {} tables for headquarter with ID: {}", filteredTables.size(), query.headquarterId());
        return filteredTables;
    }

}