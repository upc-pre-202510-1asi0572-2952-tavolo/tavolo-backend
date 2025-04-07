package com.tavolo.platform.booking.domain.model.valueobjects;

public record TableId(  Long tableId) {

    public TableId {
        if (tableId < 0) {
            throw new IllegalArgumentException("Table tableId cannot be negative");
        }
    }

    public TableId() {
        this(0L);
    }
}

