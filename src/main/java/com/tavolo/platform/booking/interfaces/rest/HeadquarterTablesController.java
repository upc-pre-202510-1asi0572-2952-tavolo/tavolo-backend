package com.tavolo.platform.booking.interfaces.rest;

import com.tavolo.platform.booking.domain.model.queries.GetTablesInHeadquarterQuery;
import com.tavolo.platform.booking.domain.services.BookingQueryService; // Interfaz
import com.tavolo.platform.booking.interfaces.rest.resources.TableResource;
import com.tavolo.platform.booking.interfaces.rest.transform.TableResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/headquarters/{headquarterId}/tables", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Headquarters", description = "Endpoints related to tables within a specific Headquarter")
@RequiredArgsConstructor
public class HeadquarterTablesController {

    private final BookingQueryService bookingQueryService; // Inyecta la interfaz del dominio

    /**
     * GET /api/v1/headquarters/{headquarterId}/tables
     * Obtiene la lista de todas las mesas pertenecientes a una sede específica.
     * Útil para que el frontend muestre las mesas disponibles para seleccionar.
     *
     * @param headquarterId El ID de la sede.
     * @return ResponseEntity con la lista de TableResource o 404 si no hay mesas.
     */
    @GetMapping
    @Operation(summary = "Get all tables for a specific headquarter",
            description = "Retrieves a list of all tables registered under the given headquarter ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tables retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = TableResource.class))),
                    @ApiResponse(responseCode = "404", description = "No tables found for this headquarter or headquarter not found", content = @Content)
            })
    public ResponseEntity<List<TableResource>> getTablesInHeadquarter(
            @Parameter(description = "ID of the Headquarter to retrieve tables from", required = true, example = "1")
            @PathVariable Long headquarterId) {

        var query = new GetTablesInHeadquarterQuery(headquarterId);
        var tables = bookingQueryService.handle(query);

        if (tables.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var resources = tables.stream()
                .map(TableResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}