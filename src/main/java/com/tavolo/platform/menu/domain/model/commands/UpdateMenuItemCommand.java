package com.tavolo.platform.menu.domain.model.commands;

import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;

import java.math.BigDecimal;

/**
 * Comando para actualizar un ítem existente del menú.
 */
public record UpdateMenuItemCommand(
        Long id,
        String name,
        String description,
        BigDecimal price,
        MenuItemCategory category,
        String imageBase64
) {
} 