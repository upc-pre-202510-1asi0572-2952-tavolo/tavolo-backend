package com.tavolo.platform.booking.interfaces.rest;

import com.tavolo.platform.booking.domain.model.queries.GetAllTablesQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTableByIdQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTableScheduleByIdAndDateQuery;
import com.tavolo.platform.booking.domain.services.TableCommandService;
import com.tavolo.platform.booking.domain.services.TableQueryService;
import com.tavolo.platform.booking.interfaces.rest.resources.AvailabilitySlotResource;
import com.tavolo.platform.booking.interfaces.rest.resources.CreateTableResource;
import com.tavolo.platform.booking.interfaces.rest.resources.TableResource;
import com.tavolo.platform.booking.interfaces.rest.transform.AvailabilitySlotResourceFromEntityAssembler;
import com.tavolo.platform.booking.interfaces.rest.transform.CreateTableCommandFromResourceAssembler;
import com.tavolo.platform.booking.interfaces.rest.transform.TableResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/tables", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Table", description = "Table Management Endpoints")
public class TableController {
    private final TableCommandService tableCommandService;
    private final TableQueryService tableQueryService;

    public TableController(TableCommandService tableCommandService, TableQueryService tableQueryService) {
        this.tableCommandService = tableCommandService;
        this.tableQueryService = tableQueryService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableResource> createTable(@RequestBody CreateTableResource createTableResource){
        var createTableCommand = CreateTableCommandFromResourceAssembler.toCommandFromResource(createTableResource);
        var table = tableCommandService.handle(createTableCommand);
        var tableResource = TableResourceFromEntityAssembler.toResourceFromEntity(table.get());
        return new ResponseEntity<>(tableResource, HttpStatus.CREATED);
    }

    @GetMapping(value = "{tableId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TableResource> getTableById(@PathVariable Long tableId) {
        var getTableByIdQuery = new GetTableByIdQuery(tableId);
        var table = tableQueryService.handle(getTableByIdQuery);
        var tableResource = TableResourceFromEntityAssembler.toResourceFromEntity(table.get());
        return new ResponseEntity<>(tableResource, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TableResource>> getAllTables() {
        var getAllTablesQuery = new GetAllTablesQuery();
        var tables = tableQueryService.handle(getAllTablesQuery);
        var headquarterResources = tables.stream()
                .map(TableResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(headquarterResources, HttpStatus.OK);
    }

    @GetMapping(value = "{tableId}/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AvailabilitySlotResource>> getTableSchedule(
            @PathVariable Long tableId,
            @RequestParam LocalDate date) {

        var getTableScheduleQuery = new GetTableScheduleByIdAndDateQuery(tableId, date);
        var availabilitySlots = tableQueryService.handle(getTableScheduleQuery);

        var availabilitySlotResources = AvailabilitySlotResourceFromEntityAssembler
                .toResourceListFromEntities(availabilitySlots);

        return new ResponseEntity<>(availabilitySlotResources, HttpStatus.OK);
    }
}
