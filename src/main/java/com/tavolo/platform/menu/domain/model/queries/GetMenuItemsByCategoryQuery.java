package com.tavolo.platform.menu.domain.model.queries;

import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;

/**
 * Consulta para obtener ítems del menú filtrados por categoría.
 */
public record GetMenuItemsByCategoryQuery(MenuItemCategory category) {
} 