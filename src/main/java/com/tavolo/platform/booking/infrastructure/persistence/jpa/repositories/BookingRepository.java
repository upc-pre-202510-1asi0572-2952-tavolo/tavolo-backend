package com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
