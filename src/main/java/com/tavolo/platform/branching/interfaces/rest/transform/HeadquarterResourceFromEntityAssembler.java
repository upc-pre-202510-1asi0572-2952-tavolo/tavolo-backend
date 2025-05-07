package com.tavolo.platform.branching.interfaces.rest.transform;

import com.tavolo.platform.branching.domain.model.aggregates.Headquarter;
import com.tavolo.platform.branching.interfaces.rest.resources.HeadquarterResource;

import java.time.format.DateTimeFormatter;

public class HeadquarterResourceFromEntityAssembler {
    public static HeadquarterResource toResourceFromEntity(Headquarter entity) {
        return new HeadquarterResource(
                entity.getId(),
                entity.getName().name(),
                entity.getContactNumbers().landlinePhone(), entity.getContactNumbers().mobilePhone(),
                entity.getCoordinates().latitude(), entity.getCoordinates().longitude(),
                entity.getAddress().getAddress(),
                entity.getOpeningTime().format(DateTimeFormatter.ofPattern("HH:mm")), // Formatea aquí
                entity.getClosingTime().format(DateTimeFormatter.ofPattern("HH:mm")), // Formatea aquí
                entity.getIntervalMinutes()
        );
    }
}
