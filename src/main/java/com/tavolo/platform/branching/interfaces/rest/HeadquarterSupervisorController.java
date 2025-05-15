package com.tavolo.platform.branching.interfaces.rest;

import com.tavolo.platform.branching.domain.model.commands.AddSupervisorToHeadquarterCommand;
import com.tavolo.platform.branching.domain.model.commands.RemoveSupervisorFromHeadquarterCommand;
import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquarterSupervisorByIdHeadquarter;
import com.tavolo.platform.branching.domain.model.queries.GetAllHeadquartersQuery;
import com.tavolo.platform.branching.domain.model.valueobjects.UserId;
import com.tavolo.platform.branching.domain.services.HeadquarterSupervisorCommandService;
import com.tavolo.platform.branching.domain.services.HeadquarterSupervisorQueryService;
import com.tavolo.platform.branching.interfaces.rest.resources.SupervisorResource;
import com.tavolo.platform.branching.interfaces.rest.transform.SupervisorResourceFromHeadquarterDataAssembler;
import com.tavolo.platform.shared.application.exceptions.ResourceNotFoundException;
import com.tavolo.platform.shared.interfaces.rest.resources.SuccessMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/headquarters", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Headquarter Supervisors", description = "Headquarter Supervisor Management Endpoints")
public class HeadquarterSupervisorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeadquarterSupervisorController.class);
    private final HeadquarterSupervisorCommandService commandService;
    private final HeadquarterSupervisorQueryService queryService;

    public HeadquarterSupervisorController(
            HeadquarterSupervisorCommandService commandService,
            HeadquarterSupervisorQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping(value ="/{headquarterId}/supervisors", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all supervisors for a headquarter", description = "Returns all supervisors assigned to a specific headquarter")
    public ResponseEntity<List<SupervisorResource>> getSupervisorsByHeadquarterId(
            @PathVariable Long headquarterId) {
        LOGGER.info("Received request to get supervisors for headquarter with ID: {}", headquarterId);

        var query = new GetAllHeadquarterSupervisorByIdHeadquarter(headquarterId);
        var supervisors = queryService.handle(query);

        var resources = supervisors.stream()
                .map(SupervisorResourceFromHeadquarterDataAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        LOGGER.info("Found {} supervisors for headquarter with ID: {}", resources.size(), headquarterId);
        return ResponseEntity.ok(resources);
    }

    @PostMapping(value = "/{headquarterId}/supervisors/{supervisorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Assign supervisor to headquarter", description = "Assigns a supervisor to a headquarter")
    public ResponseEntity<SupervisorResource> addSupervisorToHeadquarter(
            @PathVariable Long headquarterId,
            @PathVariable Long supervisorId) {
        LOGGER.info("Received request to assign supervisor {} to headquarter {}", supervisorId, headquarterId);

        var command = new AddSupervisorToHeadquarterCommand(
                headquarterId,
                new UserId(supervisorId)
        );

        var result = commandService.handle(command);

        return result.map(data -> {
            LOGGER.info("Successfully assigned supervisor {} to headquarter {}", supervisorId, headquarterId);
            return new ResponseEntity<>(
                    SupervisorResourceFromHeadquarterDataAssembler.toResourceFromEntity(data),
                    HttpStatus.CREATED
            );
        }).orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "/{headquarterId}/supervisors/{supervisorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove supervisor from headquarter", description = "Removes a supervisor from a headquarter")
    public ResponseEntity<SuccessMessage> removeSupervisorFromHeadquarter(
            @PathVariable Long headquarterId,
            @PathVariable Long supervisorId) {
        LOGGER.info("Received request to remove supervisor {} from headquarter {}", supervisorId, headquarterId);

        try {
            var command = new RemoveSupervisorFromHeadquarterCommand(
                    headquarterId,
                    new UserId(supervisorId)
            );

            var result = commandService.handle(command);

            if (result.isPresent()) {
                LOGGER.info("Supervisor con ID: {} eliminado exitosamente de la sede con ID: {}",
                        supervisorId, headquarterId);
                return ResponseEntity.ok(new SuccessMessage(HttpStatus.OK.value(),
                        "Supervisor con ID: " + supervisorId + " eliminado exitosamente de la sede con ID: " + headquarterId));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new SuccessMessage(HttpStatus.NOT_FOUND.value(),
                                "No se encontró el supervisor " + supervisorId + " en la sede " + headquarterId));
            }
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Error al eliminar supervisor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new SuccessMessage(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }
}