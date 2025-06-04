package com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.entities.AvailabilitySlot;
import com.tavolo.platform.booking.domain.model.valueobjects.HeadquarterId;
import com.tavolo.platform.booking.domain.model.valueobjects.TableStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {
    Boolean existsByHeadquarterIdAndTableDetails_TableNumber(HeadquarterId headquarterId, Integer tableDetails_tableNumber);

    Boolean existsByHeadquarterIdAndTableDetails_TableNumberAndStatusNot(HeadquarterId headquarterId, Integer tableDetails_tableNumber, TableStatus status);

    @Query("SELECT a FROM Table t JOIN t.availabilitySlots a WHERE t.id = :tableId AND a.dateOfSlot = :date")
    List<AvailabilitySlot> findAvailabilitySlotsByTableIdAndDate(@Param("tableId") Long tableId, @Param("date") LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Table t LEFT JOIN FETCH t.availabilitySlots WHERE t.id = :id")
    Optional<Table> findByIdWithSlotsForUpdate(@Param("id") Long id);

    List<Table> findByHeadquarterIdAndStatusNot(HeadquarterId headquarterId, TableStatus status);
}
