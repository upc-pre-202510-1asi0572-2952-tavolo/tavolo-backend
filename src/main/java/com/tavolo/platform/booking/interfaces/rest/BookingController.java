package com.tavolo.platform.booking.interfaces.rest;

import com.tavolo.platform.booking.domain.exceptions.SlotAlreadyBookedException;
import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;
import com.tavolo.platform.booking.domain.services.BookingCommandService;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.tavolo.platform.booking.interfaces.rest.resources.BookingResource;
import com.tavolo.platform.booking.interfaces.rest.resources.CreateBookingResource;
import com.tavolo.platform.booking.interfaces.rest.resources.ErrorResource;
import com.tavolo.platform.booking.interfaces.rest.transform.BookingResourceFromEntityAssembler;
import com.tavolo.platform.iam.interfaces.acl.IamContextFacade; // Importa la fachada de IAM
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/v1/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Bookings", description = "Endpoints for creating and managing Bookings (for today)")
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingCommandService bookingCommandService;
    private final IamContextFacade iamContextFacade; // Inyecta la fachada de IAM
    private final BookingRepository bookingRepository; // Add this line


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new booking for today",
            description = "Creates a new booking for a specific table and time slot for the current date, using the authenticated user's ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookingResource.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Booking created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingResource.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated or ID not found", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Table not found", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Slot already booked",
                            content = @Content(mediaType = "application/json", schema = @Schema(ref = "#/components/schemas/ErrorResource")))
            })
    public ResponseEntity<Object> createBooking(@Valid @RequestBody CreateBookingResource resource,
                                                UriComponentsBuilder uriBuilder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            Long userId = iamContextFacade.fetchUserIdByUsername(username);

            if (userId != null) {
                var command = new CreateBookingCommand(
                        userId,
                        resource.tableId(),
                        LocalDate.now(),
                        resource.slot());

                try {
                    log.info("Attempting to create booking with command: {}", command);
                    Long bookingId = bookingCommandService.handle(command);
                    log.info("Booking created successfully with ID: {}", bookingId);

                    return bookingRepository.findById(bookingId)
                            .map(BookingResourceFromEntityAssembler::toResourceFromEntity)
                            .map(bookingRes -> new ResponseEntity<Object>(bookingRes, HttpStatus.CREATED))
                            .orElseGet(() -> {
                                log.error("Booking created (ID: {}) but could not be retrieved from repository.", bookingId);
                                ErrorResource error = new ErrorResource("Booking created but could not be retrieved.");
                                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
                            });

                } catch (SlotAlreadyBookedException e) {
                    log.warn("Booking conflict: {}", e.getMessage());
                    return new ResponseEntity<>(new ErrorResource(e.getMessage()), HttpStatus.CONFLICT);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid argument during booking creation: {}", e.getMessage());
                    ErrorResource error = new ErrorResource(e.getMessage());
                    if (e.getMessage() != null && e.getMessage().toLowerCase().contains("not found")) {
                        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
                } catch (Exception e) {
                    log.error("Unexpected error creating booking: {}", command, e);
                    return new ResponseEntity<>(new ErrorResource("An unexpected error occurred while creating the booking."), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(new ErrorResource("No se pudo obtener el ID del usuario autenticado."), HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(new ErrorResource("Usuario no autenticado."), HttpStatus.UNAUTHORIZED);
        }
    }

    // --- Futuros Endpoints ---
    // ...
}