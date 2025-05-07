package com.tavolo.platform.booking.application.internal.commandservices;

import com.tavolo.platform.booking.application.internal.outboundedservices.acl.ExternalHeadquarterService;
import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.commands.CreateTableCommand;
import com.tavolo.platform.booking.domain.model.commands.CreateTableScheduleCommand;
import com.tavolo.platform.booking.domain.model.entities.AvailabilitySlot;
import com.tavolo.platform.booking.domain.model.events.SingleTableAvailabilitySlotsGeneratedEvent;
import com.tavolo.platform.booking.domain.model.valueobjects.HeadquarterId;
import com.tavolo.platform.booking.domain.model.valueobjects.TableDetails;
import com.tavolo.platform.booking.domain.model.valueobjects.TimeSlot;
import com.tavolo.platform.booking.domain.services.TableCommandService;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.TableRepository;
import com.tavolo.platform.shared.application.exceptions.ResourceAlreadyException;
import com.tavolo.platform.shared.application.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

@Service
public class TableCommandServiceImpl implements TableCommandService {
    private final TableRepository tableRepository;
    private final ExternalHeadquarterService externalHeadquarterService;
    private final ApplicationEventPublisher eventPublisher; // Nuevo
    private static final Logger LOGGER = LoggerFactory.getLogger(TableCommandServiceImpl.class);

    public TableCommandServiceImpl(TableRepository tableRepository, ExternalHeadquarterService externalHeadquarterService, ApplicationEventPublisher eventPublisher) {
        this.tableRepository = tableRepository;
        this.externalHeadquarterService = externalHeadquarterService;
        this.eventPublisher = eventPublisher; // Nuevo
    }

    @Override
    public Optional<Table> handle(CreateTableCommand command) {
        LOGGER.info("Starting to process create table command with headquarters ID: {} and table number: {}", command.headquartersId(), command.tableNumber());
        var headquarterId = new HeadquarterId(command.headquartersId());

        LOGGER.debug("Checking if headquarters with ID: {} exists", command.headquartersId());
        if(!externalHeadquarterService.existsHeadquarter(command.headquartersId())) {
            LOGGER.error("Headquarters with ID: {} not found", command.headquartersId());
            throw new ResourceNotFoundException("Headquarter with ID: " + command.headquartersId() + " not found");
        }

        LOGGER.debug("Checking if table with number: {} already exists in headquarters: {}", command.tableNumber(), command.headquartersId());
        if(tableRepository.existsByHeadquarterIdAndTableDetails_TableNumber(headquarterId, command.tableNumber())) {
            LOGGER.warn("Table with number: {} already exists in headquarters: {}", command.tableNumber(), command.headquartersId());
            throw new ResourceAlreadyException("Table with number: " + command.tableNumber() + " already exists in headquarters: " + command.headquartersId());
        }

        LOGGER.info("Creating new table with number: {} in headquarters: {}", command.tableNumber(), command.headquartersId());
        var tableDetails = new TableDetails(command.tableNumber(), command.seats());
        var table = new Table(tableDetails, headquarterId);

        try {
            LOGGER.debug("Saving table to repository");
            var savedTable = tableRepository.save(table);
            LOGGER.info("Table created successfully with ID: {}", table.getId());

            // Generar slots de disponibilidad automáticamente
            //table.generateAvailabilitySlots();
            // Publicar evento manualmente después del guardado
            eventPublisher.publishEvent(
                    new SingleTableAvailabilitySlotsGeneratedEvent(this, savedTable.getId()));
            LOGGER.info("Generated availability slots for table with ID: {}", savedTable.getId());

        } catch (Exception e) {
            LOGGER.error("Error while saving table: {}", e.getMessage(), e);
            throw new RuntimeException("Error while saving table: " + e.getMessage());
        }

        return Optional.of(table);
    }

    @Override
    public Optional<Table> handle(CreateTableScheduleCommand command) {
        LOGGER.info("Starting table schedule creation for table ID: {}", command.tableId());

        var table = tableRepository.findById(command.tableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with ID: " + command.tableId() + " not found"));

        var headquarterId = table.getHeadquarterId();

        if(!table.getAvailabilitySlots().isEmpty()) {
            LOGGER.warn("Table with ID: {} already has availability slots", command.tableId());
            throw new RuntimeException("Table with ID: " + command.tableId() + " already has availability slots");
        }

        LOGGER.debug("Checking if headquarters with ID: {} exists", headquarterId.headquarterId());
        if(!externalHeadquarterService.existsHeadquarter(headquarterId.headquarterId())) {
            LOGGER.error("Headquarters with ID: {} not found", headquarterId.headquarterId());
            throw new ResourceNotFoundException("Headquarter with ID: " + headquarterId.headquarterId() + " not found");
        }

        var headquarterOpeningTimeOpt = externalHeadquarterService.getHeadquarterOpeningTime(headquarterId.headquarterId());
        var headquarterClosingTimeOpt = externalHeadquarterService.getHeadquarterClosingTime(headquarterId.headquarterId());

        if(headquarterOpeningTimeOpt.get() == LocalTime.MIN && headquarterClosingTimeOpt.get() == LocalTime.MAX) {
            LOGGER.error("Error while retrieving headquarters schedules");
            throw new RuntimeException("Error while retrieving opening and closing times for headquarters");
        }

        var intervalMinutesOpt = externalHeadquarterService.getHeadquarterIntervalMinutes(headquarterId.headquarterId());

        if(!intervalMinutesOpt.isPresent() || intervalMinutesOpt.get() == 0) {
            LOGGER.error("Error while retrieving interval minutes for headquarters");
            throw new RuntimeException("Error while retrieving interval minutes for headquarters");
        }

        LocalTime openingTime = headquarterOpeningTimeOpt.get();
        LocalTime closingTime = headquarterClosingTimeOpt.get();
        int intervalMinutes = intervalMinutesOpt.get();

        LOGGER.info("Generating slots from {} to {} with {} minutes intervals",
                openingTime, closingTime, intervalMinutes);

        // Obtener fecha actual
        LocalDate today = LocalDate.now();

        // Calcular el próximo domingo
        LocalDate nextSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Generar slots para cada día hasta el domingo
        for (LocalDate currentDate = today; !currentDate.isAfter(nextSunday); currentDate = currentDate.plusDays(1)) {
            LOGGER.debug("Generating slots for day: {}", currentDate);

            // Generar slots para este día
            LocalTime currentTime = openingTime;
            while (currentTime.isBefore(closingTime)) {
                LocalTime endTime = currentTime.plusMinutes(intervalMinutes);

                if (endTime.isAfter(closingTime)) {
                    endTime = closingTime;
                }

                // Crear TimeSlot
                TimeSlot timeSlot = new TimeSlot(currentTime, endTime);

                // Crear AvailabilitySlot
                AvailabilitySlot slot = new AvailabilitySlot(currentDate, timeSlot);

                // Añadir el slot a la mesa
                table.getAvailabilitySlots().add(slot);

                // Avanzar al siguiente intervalo
                currentTime = endTime;
            }
        }

        LOGGER.info("Created {} availability slots for the table", table.getAvailabilitySlots().size());

        // Guardar la mesa con sus nuevos slots
        tableRepository.save(table);

        return Optional.of(table);
    }
}