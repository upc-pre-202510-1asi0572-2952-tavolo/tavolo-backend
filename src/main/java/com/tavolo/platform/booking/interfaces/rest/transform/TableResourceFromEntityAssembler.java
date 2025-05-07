package com.tavolo.platform.booking.interfaces.rest.transform;

import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.interfaces.rest.resources.TableResource;

public class TableResourceFromEntityAssembler {
    public static TableResource toResourceFromEntity(Table entity){
        return new TableResource(
                entity.getId(),
                entity.getHeadquarterId().headquarterId(),
                entity.getTableDetails().tableNumber(),
                entity.getTableDetails().seats(),
                entity.getStatus().toString()
        );
    }
}
