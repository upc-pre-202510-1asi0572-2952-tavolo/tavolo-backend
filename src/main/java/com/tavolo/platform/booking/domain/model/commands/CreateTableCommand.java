package com.tavolo.platform.booking.domain.model.commands;

public record CreateTableCommand(Integer tableNumber, Integer seats, Long headquartersId, String zone) {

}
