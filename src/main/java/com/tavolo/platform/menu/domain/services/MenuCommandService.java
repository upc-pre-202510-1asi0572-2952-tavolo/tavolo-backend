package com.tavolo.platform.menu.domain.services;

import com.tavolo.platform.menu.domain.model.aggregates.MenuItem;
import com.tavolo.platform.menu.domain.model.commands.CreateMenuItemCommand;
import com.tavolo.platform.menu.domain.model.commands.DeleteMenuItemCommand;
import com.tavolo.platform.menu.domain.model.commands.UpdateMenuItemCommand;

import java.util.Optional;

/**
 * Interfaz para el servicio de comandos relacionados con el menú.
 */
public interface MenuCommandService {
    /**
     * Maneja el comando para crear un nuevo ítem del menú.
     * @param command Comando con los datos para crear el ítem
     * @return Optional con el ítem creado o vacío si hubo un error
     */
    Optional<MenuItem> handle(CreateMenuItemCommand command);

    /**
     * Maneja el comando para actualizar un ítem del menú existente.
     * @param command Comando con los datos actualizados
     * @return Optional con el ítem actualizado o vacío si no existe o hubo un error
     */
    Optional<MenuItem> handle(UpdateMenuItemCommand command);

    /**
     * Maneja el comando para eliminar un ítem del menú.
     * @param command Comando con el ID del ítem a eliminar
     * @return Optional con el ítem eliminado o vacío si no existe
     */
    Optional<MenuItem> handle(DeleteMenuItemCommand command);
} 