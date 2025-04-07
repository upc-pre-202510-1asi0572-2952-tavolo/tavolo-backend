package com.tavolo.platform.booking.application.internal.commandservices;

import com.tavolo.platform.booking.domain.exceptions.SlotAlreadyBookedException;
import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.commands.CreateBookingCommand;
import com.tavolo.platform.booking.domain.model.valueobjects.BookingSlot;
import com.tavolo.platform.booking.domain.model.valueobjects.BookingStatus;
import com.tavolo.platform.booking.domain.services.BookingCommandService;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.TableRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingCommandServiceImpl implements BookingCommandService {

    private static final Logger log = LoggerFactory.getLogger(BookingCommandServiceImpl.class);
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;

    @Override
    @Transactional
    public Long handle(CreateBookingCommand command) {
        log.info("Handling CreateBookingCommand for user {}, table {} and slot {} on date {}",
                command.userId(), command.tableId(), command.slot(), command.date());

        // --- Validación Regla de Negocio: Solo reservas para hoy ---
        if (!command.date().isEqual(LocalDate.now())) {
            log.warn("Attempted booking for a non-current date: {}", command.date());
            throw new IllegalArgumentException("Bookings can only be made for the current date: " + LocalDate.now());
        }

        // --- Validación: Mesa Existe y Obtener HeadquarterId ---
        var table = tableRepository.findById(command.tableId())
                .orElseThrow(() -> {
                    log.warn("Table not found for ID: {}", command.tableId());
                    return new IllegalArgumentException("Table with ID " + command.tableId() + " not found.");
                });
        Long actualHeadquarterId = table.getHeadquarterId();
        log.debug("Table found: ID={}, HQ_ID={}", table.getId(), actualHeadquarterId);

        // --- Validación CRÍTICA: Doble Reserva ---
        BookingSlot bookingSlot = new BookingSlot(command.date(), command.slot());
        boolean alreadyBooked = bookingRepository.existsByTableIdAndBookingSlotAndStatus(
                command.tableId(),
                bookingSlot,
                BookingStatus.CONFIRMED
        );

        if (alreadyBooked) {
            log.warn("Slot already booked for table {}, slot {}, date {}",
                    command.tableId(), command.slot(), command.date());
            throw new SlotAlreadyBookedException(command.tableId(), command.slot().name(), command.date());
        }
        log.debug("Slot is available for table {}, slot {}, date {}",
                command.tableId(), command.slot(), command.date());

        // --- Creación y Persistencia ---
        Booking booking = new Booking(command.userId(), command.tableId(), actualHeadquarterId, bookingSlot);
        bookingRepository.save(booking);
        log.info("Booking created successfully with ID: {}", booking.getId());

        // Publicar evento ...

        return booking.getId();
    }

    // Implementación de otros métodos de la interfaz (si los hubiera)...
}