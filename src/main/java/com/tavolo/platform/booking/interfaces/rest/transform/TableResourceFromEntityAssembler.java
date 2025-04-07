package com.tavolo.platform.booking.interfaces.rest.transform;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.interfaces.rest.resources.TableResource;

/**
 * Assembler para convertir la entidad Table a su DTO Resource.
 */
public class TableResourceFromEntityAssembler {
    public static TableResource toResourceFromEntity(Table entity) {
        if (entity == null) return null;
        return new TableResource(
                entity.getId(),
                entity.getIdentifier(),
                entity.getCapacity()
        );
    }
}