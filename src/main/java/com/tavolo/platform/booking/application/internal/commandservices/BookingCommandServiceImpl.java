package com.tavolo.platform.booking.application.internal.commandservices;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;
import com.tavolo.platform.booking.domain.model.entities.AvailabilitySlot;
import com.tavolo.platform.booking.domain.model.entities.BookingSlot;
import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlotStatus;
import com.tavolo.platform.booking.domain.model.valueobjects.UserId;
import com.tavolo.platform.booking.domain.services.BookingCommandService;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.TableRepository;
import com.tavolo.platform.booking.application.internal.outboundedservices.acl.ExternalUserService;
import com.tavolo.platform.shared.application.exceptions.InvalidValueException;
import com.tavolo.platform.shared.application.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingCommandServiceImpl implements BookingCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingCommandServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;
    private final ExternalUserService externalUserService;

    public BookingCommandServiceImpl(BookingRepository bookingRepository,
                                     ExternalUserService externalUserService,
                                     TableRepository tableRepository) {
        this.bookingRepository = bookingRepository;
        this.externalUserService = externalUserService;
        this.tableRepository = tableRepository;
    }

    @Override
    @Transactional
    public Optional<Booking> handle(CreateBookingCommand command) {
        if (!externalUserService.existUserById(command.clientId())) {
            throw new IllegalArgumentException("User does not exist");
        }

        // Retrieve the table with a pessimistic lock to avoid concurrent modifications
        Table table = tableRepository.findByIdWithSlotsForUpdate(command.tableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with ID: " + command.tableId()));

        // Filter AvailabilitySlots for the specified date and matching the requested slot IDs
        List<AvailabilitySlot> slotsForDate = table.getAvailabilitySlots().stream()
                .filter(slot -> slot.getDateOfSlot().equals(command.bookingDate())
                        && command.slotIds().contains(slot.getId()))
                .collect(Collectors.toList());

        // Verify that all requested slots have been found
        if (slotsForDate.size() != command.slotIds().size()) {
            throw new ResourceNotFoundException("Some requested availability slots were not found");
        }

        // Validate that each slot is in AVAILABLE state
        boolean allAvailable = slotsForDate.stream()
                .allMatch(slot -> slot.getStatus().equals(ScheduleSlotStatus.AVAILABLE));
        if (!allAvailable) {
            throw new InvalidValueException("One or more slots are not available for booking.");
        }

        // Sort the slots by startTime
        slotsForDate.sort((s1, s2) ->
                s1.getTimeInterval().startTime().compareTo(s2.getTimeInterval().startTime()));

        // Validate that the slots are consecutive
        for (int i = 1; i < slotsForDate.size(); i++) {
            LocalTime endPrev = slotsForDate.get(i - 1).getTimeInterval().endTime();
            LocalTime startCurrent = slotsForDate.get(i).getTimeInterval().startTime();
            if (!endPrev.equals(startCurrent)) {
                throw new InvalidValueException("The slots must be consecutive.");
            }
        }

        // Calculate the total duration in minutes
        LocalTime startTime = slotsForDate.get(0).getTimeInterval().startTime();
        LocalTime endTime = slotsForDate.get(slotsForDate.size() - 1).getTimeInterval().endTime();
        long totalMinutes = Duration.between(startTime, endTime).toMinutes();
        if (totalMinutes > 120) {
            throw new InvalidValueException("Cannot book more than 2 hours. Requested duration: " + totalMinutes + " minutes.");
        }
        LOGGER.info("Total booking duration: {} minutes", totalMinutes);

        // Create BookingSlots from each AvailabilitySlot and update its status to RESERVED
        Set<BookingSlot> bookingSlots = new HashSet<>();
        for (AvailabilitySlot slot : slotsForDate) {
            BookingSlot bookingSlot = new BookingSlot(slot.getTimeInterval());
            bookingSlots.add(bookingSlot);
            // Because of the pessimistic lock, no other transaction can update this slot concurrently.
            slot.updateStatus(ScheduleSlotStatus.RESERVED);
        }

        // Create the Booking entity
        Booking booking = new Booking(
                new UserId(command.clientId()),
                table,
                command.bookingDate(),
                bookingSlots
        );

        // Persist the booking (changes in table.availabilitySlots are cascaded)
        bookingRepository.save(booking);
        tableRepository.save(table);

        LOGGER.info("Booking created successfully with ID: {}", booking.getId());
        return Optional.of(booking);
    }
}
