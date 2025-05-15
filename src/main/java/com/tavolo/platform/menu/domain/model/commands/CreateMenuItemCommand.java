package com.tavolo.platform.menu.domain.model.commands;

import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;

import java.math.BigDecimal;

/**
 * Comando para la creación de un nuevo ítem del menú.
 */
public record CreateMenuItemCommand(
        String name,
        String description,
        BigDecimal price,
        MenuItemCategory category,
        String imageBase64
) {
} 