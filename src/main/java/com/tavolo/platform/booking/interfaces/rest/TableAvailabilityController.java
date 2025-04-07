package com.tavolo.platform.booking.interfaces.rest;

import com.tavolo.platform.booking.domain.model.queries.GetTableSlotAvailabilityQuery;
import com.tavolo.platform.booking.domain.services.BookingQueryService; // Interfaz
import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot;
import com.tavolo.platform.booking.interfaces.rest.resources.SlotStatusResource;
import com.tavolo.platform.booking.interfaces.rest.resources.TableSlotAvailabilityResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator; // Para ordenar los slots
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/tables/{tableId}/availability", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tables", description = "Endpoints related to Table availability")
@RequiredArgsConstructor
public class TableAvailabilityController {

    private final BookingQueryService bookingQueryService; // Inyecta la interfaz del dominio

    /**
     * GET /api/v1/tables/{tableId}/availability
     * Obtiene la disponibilidad de todos los slots horarios para una mesa específica, SÓLO para el día actual.
     *
     * @param tableId El ID de la mesa a consultar.
     * @return ResponseEntity con TableSlotAvailabilityResource o 404/400 si hay error.
     */
    @GetMapping
    @Operation(summary = "Get slot availability for a specific table for today",
            description = "Retrieves the availability status (AVAILABLE/RESERVED) for all defined time slots for the given table ID, specifically for the current date.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Availability retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TableSlotAvailabilityResource.class))),
                    @ApiResponse(responseCode = "404", description = "Table not found", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid request (should not happen if date is always today)", content = @Content)
            })
    public ResponseEntity<?> getTableAvailabilityForToday(
            @Parameter(description = "ID of the Table to check availability for", required = true, example = "5")
            @PathVariable Long tableId) {

        LocalDate today = LocalDate.now();
        var query = new GetTableSlotAvailabilityQuery(tableId, today);

        try {
            // Llama al servicio de consulta
            Map<ScheduleSlot, String> availabilityMap = bookingQueryService.handle(query);

            // Convierte el mapa a la lista de recursos DTO para la respuesta
            List<SlotStatusResource> slotResources = availabilityMap.entrySet().stream()
                    .map(entry -> new SlotStatusResource(
                            entry.getKey(),
                            entry.getKey().getDescription(),
                            entry.getValue()
                    ))
                    .sorted(Comparator.comparing(SlotStatusResource::slot)) // Ordena por el Enum
                    .toList();

            // Construye el recurso de respuesta final
            var resource = new TableSlotAvailabilityResource(tableId, today, slotResources);
            return ResponseEntity.ok(resource);

        } catch (IllegalArgumentException e) {
            // Captura errores como "Table not found" o fecha inválida
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Captura otros errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}