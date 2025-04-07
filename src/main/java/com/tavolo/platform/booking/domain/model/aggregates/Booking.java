package com.tavolo.platform.booking.domain.model.aggregates;

import com.tavolo.platform.booking.domain.model.valueobjects.BookingSlot;
import com.tavolo.platform.booking.domain.model.valueobjects.BookingStatus;
import com.tavolo.platform.shared.domain.model.entities.AuditableModel; // Asumiendo que usas esto
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "Bookings")
// @Getter // Comentado o quitado, definiremos getters manualmente
@NoArgsConstructor // Necesario para JPA
// Constraint UNIQUE crucial


@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"table_id", "booking_date", "booking_slot"})
})
public class Booking extends AuditableModel { // Asegúrate que hereda de AuditableModel si es así

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // ID del usuario que reserva

    @Column(name = "table_id", nullable = false)
    private Long tableId; // ID de la mesa reservada

    @Column(nullable = false)
    private Long headquarterId; // ID de la sede (obtenido de la mesa)

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "booking_date", nullable = false)),
            @AttributeOverride(name = "slot", column = @Column(name = "booking_slot", nullable = false))
    })
    private BookingSlot bookingSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status;

    // --- Constructor ---
    public Booking(Long userId, Long tableId, Long headquarterId, BookingSlot bookingSlot) {
        this.userId = userId;
        this.tableId = tableId;
        this.headquarterId = headquarterId;
        this.bookingSlot = bookingSlot;
        this.status = BookingStatus.CONFIRMED;
    }

    // --- Lógica de Dominio ---
    public void cancel() {
        if (this.status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled. Current status: " + this.status);
        }
        this.status = BookingStatus.CANCELLED;
    }

    // --- GETTERS EXPLÍCITOS ---

    public Long getId() { // <<<--- MÉTODO EXPLÍCITO PARA ID
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTableId() {
        return tableId;
    }

    public Long getHeadquarterId() {
        return headquarterId;
    }

    public BookingSlot getBookingSlot() {
        return bookingSlot;
    }

    public BookingStatus getStatus() {
        return status;
    }

    // Considera añadir setters solo si son estrictamente necesarios y encapsulan lógica.
}