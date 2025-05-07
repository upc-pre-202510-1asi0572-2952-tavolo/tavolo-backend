package com.tavolo.platform.branching.interfaces.rest.resources;

public record HeadquarterResource(
        Long id,
        String name,
        String landlinePhone, String mobilePhone,
        Double latitude, Double longitude,
        String streetAddress,
        String openingTime, String closingTime,
        Integer intervalMinutes
) {
}
