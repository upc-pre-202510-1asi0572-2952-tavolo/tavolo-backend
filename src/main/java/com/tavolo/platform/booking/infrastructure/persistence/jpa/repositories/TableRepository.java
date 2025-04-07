package com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import org.springframework.data.jpa.repository.JpaRepository; // O interfaz base propia
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Marca para Spring
public interface TableRepository extends JpaRepository<Table, Long> { // Usamos JpaRepository por conveniencia

    List<Table> findByHeadquarterId(Long headquarterId);

    // Podríamos añadir más métodos específicos si fueran necesarios
}