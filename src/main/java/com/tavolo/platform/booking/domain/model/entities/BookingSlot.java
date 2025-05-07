package com.tavolo.platform.booking.domain.model.entities;

import com.tavolo.platform.booking.domain.model.valueobjects.TimeSlot;
import com.tavolo.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BookingSlot extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TimeSlot timeInterval;

    protected BookingSlot() {
        // Constructor para JPA
    }

    public BookingSlot(TimeSlot timeInterval) {
        this.timeInterval = timeInterval;
    }

}
