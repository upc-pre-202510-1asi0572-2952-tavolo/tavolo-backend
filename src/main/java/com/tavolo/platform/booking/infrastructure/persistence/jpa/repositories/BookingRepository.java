package com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.booking.domain.model.aggregates.Booking;
import com.tavolo.platform.booking.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserId(UserId userId);
}
