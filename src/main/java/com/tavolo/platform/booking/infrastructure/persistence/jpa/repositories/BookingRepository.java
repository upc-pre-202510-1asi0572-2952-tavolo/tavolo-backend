package com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories;


import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.valueobjects.BookingSlot;
import com.tavolo.platform.booking.domain.model.valueobjects.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Método CRÍTICO para validar doble reserva
    boolean existsByTableIdAndBookingSlotAndStatus(Long tableId, BookingSlot bookingSlot, BookingStatus status);

    // Método para obtener reservas para la consulta de disponibilidad
    List<Booking> findByTableIdAndBookingSlot_DateAndStatus(Long tableId, LocalDate date, BookingStatus status);

    // Podríamos necesitar buscar por usuario, etc. en el futuro
    // List<Booking> findByUserId(Long userId);
}