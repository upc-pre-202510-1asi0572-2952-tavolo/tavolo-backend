package com.tavolo.platform.menu.interfaces.rest.transform;

import com.tavolo.platform.menu.domain.model.aggregates.MenuItem;
import com.tavolo.platform.menu.interfaces.rest.resources.MenuItemResource;

/**
 * Transformador para convertir entidades MenuItem en recursos MenuItemResource.
 */
public class MenuItemResourceFromEntityAssembler {
    
    public static MenuItemResource toResourceFromEntity(MenuItem entity) {
        return new MenuItemResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory().name(),
                entity.getImageBase64()
        );
    }
}
