package com.tavolo.platform.menu.interfaces.rest.transform;

import com.tavolo.platform.menu.domain.model.commands.UpdateMenuItemCommand;
import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;
import com.tavolo.platform.menu.interfaces.rest.resources.UpdateMenuItemResource;

/**
 * Transformador para convertir recursos UpdateMenuItemResource en comandos UpdateMenuItemCommand.
 */
public class UpdateMenuItemCommandFromResourceAssembler {
    
    public static UpdateMenuItemCommand toCommandFromResource(Long id, UpdateMenuItemResource resource) {
        return new UpdateMenuItemCommand(
                id,
                resource.name(),
                resource.description(),
                resource.price(),
                MenuItemCategory.valueOf(resource.category()),
                resource.imageBase64()
        );
    }
}
