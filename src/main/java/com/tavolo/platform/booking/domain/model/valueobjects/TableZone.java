package com.tavolo.platform.booking.domain.model.valueobjects;

public enum TableZone {
    MAIN_HALL("sala principal"),
    WINDOW("ventana"),
    TERRACE("terraza");

    private final String name;

    TableZone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    // Para convertir de String a enum
    public static TableZone fromString(String text) {
        for (TableZone zone : TableZone.values()) {
            if (zone.name.equalsIgnoreCase(text)) {
                return zone;
            }
        }
        return MAIN_HALL; // valor por defecto
    }
} 