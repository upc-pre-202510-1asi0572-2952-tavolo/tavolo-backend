package com.tavolo.platform.branching.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.branching.domain.model.entities.HeadquarterSupervisor;
import com.tavolo.platform.branching.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadquarterSupervisorRepository extends JpaRepository<HeadquarterSupervisor, Long> {

    // Buscar todos los supervisores por el ID de la sede
    List<HeadquarterSupervisor> findByHeadquarterId(Long headquarterId);

    // Buscar todas las sedes asignadas a un supervisor
    List<HeadquarterSupervisor> findByUserId(UserId userId);

    // Eliminar una asignación por IDs
    void deleteByHeadquarterIdAndUserId(Long headquarterId, UserId userId);
}