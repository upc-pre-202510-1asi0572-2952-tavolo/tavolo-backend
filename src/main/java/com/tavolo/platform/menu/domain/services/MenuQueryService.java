package com.tavolo.platform.menu.domain.services;

import com.tavolo.platform.menu.domain.model.aggregates.MenuItem;
import com.tavolo.platform.menu.domain.model.queries.GetAllMenuItemsQuery;
import com.tavolo.platform.menu.domain.model.queries.GetMenuItemByIdQuery;
import com.tavolo.platform.menu.domain.model.queries.GetMenuItemsByCategoryQuery;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de consultas relacionadas con el menú.
 */
public interface MenuQueryService {
    /**
     * Maneja la consulta para obtener todos los ítems del menú.
     * @param query Consulta para obtener todos los ítems
     * @return Lista de ítems del menú
     */
    List<MenuItem> handle(GetAllMenuItemsQuery query);

    /**
     * Maneja la consulta para obtener un ítem específico por su ID.
     * @param query Consulta con el ID del ítem a obtener
     * @return Optional con el ítem encontrado o vacío si no existe
     */
    Optional<MenuItem> handle(GetMenuItemByIdQuery query);

    /**
     * Maneja la consulta para obtener ítems filtrados por categoría.
     * @param query Consulta con la categoría para filtrar
     * @return Lista de ítems que pertenecen a la categoría especificada
     */
    List<MenuItem> handle(GetMenuItemsByCategoryQuery query);
} 