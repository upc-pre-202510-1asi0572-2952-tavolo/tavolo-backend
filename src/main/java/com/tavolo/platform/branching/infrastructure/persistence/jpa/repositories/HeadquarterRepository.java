package com.tavolo.platform.branching.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.branching.domain.model.aggregates.Headquarter;
import com.tavolo.platform.branching.domain.model.valueobjects.Coordinates;
import com.tavolo.platform.branching.domain.model.valueobjects.NameHeadquarter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {
    Boolean existsByName (NameHeadquarter name);
    Boolean existsByCoordinates (Coordinates coordinates);
}
