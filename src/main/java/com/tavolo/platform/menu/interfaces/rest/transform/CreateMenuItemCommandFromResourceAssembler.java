package com.tavolo.platform.menu.interfaces.rest.transform;

import com.tavolo.platform.menu.domain.model.commands.CreateMenuItemCommand;
import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;
import com.tavolo.platform.menu.interfaces.rest.resources.CreateMenuItemResource;

/**
 * Transformador para convertir recursos CreateMenuItemResource en comandos CreateMenuItemCommand.
 */
public class CreateMenuItemCommandFromResourceAssembler {
    
    public static CreateMenuItemCommand toCommandFromResource(CreateMenuItemResource resource) {
        return new CreateMenuItemCommand(
                resource.name(),
                resource.description(),
                resource.price(),
                MenuItemCategory.valueOf(resource.category()),
                resource.imageBase64()
        );
    }
}
