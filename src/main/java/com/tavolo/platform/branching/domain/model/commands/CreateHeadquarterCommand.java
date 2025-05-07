package com.tavolo.platform.branching.domain.model.commands;

import java.time.LocalTime;

public record CreateHeadquarterCommand(String name,
                                       String landlinePhone, String mobilePhone,
                                       Double latitude, Double longitude,
                                       String street, String number, String city, String postalCode, String country,
                                       LocalTime openingTime, LocalTime closingTime,
                                       Integer intervalMinutes) {
}
