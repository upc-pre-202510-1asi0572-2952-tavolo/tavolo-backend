package com.tavolo.platform.booking.domain.model.aggregates;

import com.tavolo.platform.booking.domain.model.entities.AvailabilitySlot;
import com.tavolo.platform.booking.domain.model.events.SingleTableAvailabilitySlotsGeneratedEvent;
import com.tavolo.platform.booking.domain.model.valueobjects.HeadquarterId;
import com.tavolo.platform.booking.domain.model.valueobjects.TableDetails;
import com.tavolo.platform.booking.domain.model.valueobjects.TableStatus;
import com.tavolo.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Table extends AuditableAbstractAggregateRoot<Table> {

    @Embedded
    private TableDetails tableDetails;

    // Referencia a la sede (headquarters) de la cafetería a la que pertenece la mesa
    @Column(nullable = false)
    @Embedded
    private HeadquarterId headquarterId;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    // Cada mesa posee un conjunto de ScheduleSlot generados para cada día
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_id")
    private Set<AvailabilitySlot> availabilitySlots;

    protected Table() {
        // Constructor para JPA
    }

    public Table(TableDetails tableDetails, HeadquarterId headquarterId) {
        this.tableDetails = tableDetails;
        this.headquarterId = headquarterId;
        this.availabilitySlots = new HashSet<>();
        this.status = TableStatus.AVAILABLE;
    }
}
