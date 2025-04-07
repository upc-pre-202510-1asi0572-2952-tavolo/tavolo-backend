package com.tavolo.platform.booking.application.internal.queryservices;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.queries.GetTableSlotAvailabilityQuery;
import com.tavolo.platform.booking.domain.model.queries.GetTablesInHeadquarterQuery;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.BookingRepository;
import com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.TableRepository;
import com.tavolo.platform.booking.domain.model.valueobjects.BookingStatus;
import com.tavolo.platform.booking.domain.model.valueobjects.ScheduleSlot;
import com.tavolo.platform.booking.domain.services.BookingQueryService; // Importa la interfaz del dominio
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Las consultas no modifican datos
public class BookingQueryServiceImpl implements BookingQueryService { // Implementa la interfaz

    private final TableRepository tableRepository;
    private final BookingRepository bookingRepository;

    @Override // Añadido @Override
    public List<Table> handle(GetTablesInHeadquarterQuery query) {
        return tableRepository.findByHeadquarterId(query.headquarterId());
    }

    @Override // Añadido @Override
    public Map<ScheduleSlot, String> handle(GetTableSlotAvailabilityQuery query) {

        // --- Validación/Aserción: Solo consulta para hoy ---
        if (!query.date().isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("Availability check is only allowed for the current date: " + LocalDate.now());
        }

        // 1. Validar que la tabla existe (opcional, pero bueno)
        if (!tableRepository.existsById(query.tableId())) {
            throw new IllegalArgumentException("Table with ID " + query.tableId() + " not found.");
        }

        // 2. Obtener todas las reservas confirmadas para esa tabla y fecha (hoy)
        List<Booking> confirmedBookings = bookingRepository.findByTableIdAndBookingSlot_DateAndStatus(
                query.tableId(),
                query.date(), // Siempre será hoy
                BookingStatus.CONFIRMED
        );

        // 3. Crear un mapa de los slots reservados para búsqueda rápida
        Map<ScheduleSlot, Boolean> reservedSlots = confirmedBookings.stream()
                .collect(Collectors.toMap(
                        booking -> booking.getBookingSlot().slot(), // Key: El Slot
                        booking -> true,                            // Value: true (reservado)
                        (existing, replacement) -> existing)); // Manejo de duplicados

        // 4. Construir el resultado para TODOS los slots del día
        // Usamos LinkedHashMap para mantener el orden de los slots del Enum
        return Arrays.stream(ScheduleSlot.values())
                .collect(Collectors.toMap(
                        slot -> slot, // Key: El Slot enum
                        slot -> reservedSlots.getOrDefault(slot, false) ? "RESERVED" : "AVAILABLE", // Value
                        (v1, v2) -> v1, // No debería haber colisiones
                        java.util.LinkedHashMap::new // Para mantener orden
                ));
    }

    // Aquí implementarías los handlers para otros queries si los hubiera
}