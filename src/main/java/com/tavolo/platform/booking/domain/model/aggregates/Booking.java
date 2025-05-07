package com.tavolo.platform.booking.domain.model.aggregates;

import com.tavolo.platform.booking.domain.model.entities.BookingSlot;
import com.tavolo.platform.booking.domain.model.valueobjects.UserId;
import com.tavolo.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Entity
public class Booking extends AuditableAbstractAggregateRoot<Booking> {

    // Identificador del cliente (Value Object)
    @Column(name = "client_id", nullable = false)
    @Embedded
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Table tableId;

    // Fecha de la reserva (para agrupar y filtrar reservas por día)
    @Column(nullable = false)
    private LocalDate bookingDate;

    // Los intervalos reservados (BookingSlot es una entidad de detalle )
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Set<BookingSlot> bookingSlots;

    protected Booking() {
        // Constructor para JPA
    }

    public Booking(UserId userId, Table table, LocalDate bookingDate, Set<BookingSlot> bookingSlots) {
        this.userId = userId;
        this.tableId = table;
        this.bookingDate = bookingDate;
        this.bookingSlots = bookingSlots;
    }

    // Aquí se pueden agregar reglas de dominio adicionales, por ejemplo,
    // la validación de que la reserva no supere las 2 horas totales.

}
