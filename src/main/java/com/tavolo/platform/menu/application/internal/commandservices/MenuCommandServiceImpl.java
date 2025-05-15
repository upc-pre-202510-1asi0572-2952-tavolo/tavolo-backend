package com.tavolo.platform.menu.application.internal.commandservices;

import com.tavolo.platform.menu.domain.model.aggregates.MenuItem;
import com.tavolo.platform.menu.domain.model.commands.CreateMenuItemCommand;
import com.tavolo.platform.menu.domain.model.commands.DeleteMenuItemCommand;
import com.tavolo.platform.menu.domain.model.commands.UpdateMenuItemCommand;
import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemDetails;
import com.tavolo.platform.menu.domain.services.MenuCommandService;
import com.tavolo.platform.menu.infrastructure.persistence.jpa.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio de comandos para el menú.
 */
@Service
public class MenuCommandServiceImpl implements MenuCommandService {
    private final MenuItemRepository menuItemRepository;

    public MenuCommandServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional
    public Optional<MenuItem> handle(CreateMenuItemCommand command) {
        var details = new MenuItemDetails(
                command.name(),
                command.description(),
                command.price(),
                command.imageBase64()
        );
        var menuItem = new MenuItem(details, command.category());
        return Optional.of(menuItemRepository.save(menuItem));
    }

    @Override
    @Transactional
    public Optional<MenuItem> handle(UpdateMenuItemCommand command) {
        return menuItemRepository.findById(command.id()).map(existingItem -> {
            // Eliminamos el item existente y creamos uno nuevo con el mismo ID
            menuItemRepository.delete(existingItem);
            
            var updatedDetails = new MenuItemDetails(
                    command.name(),
                    command.description(),
                    command.price(),
                    command.imageBase64()
            );
            var updatedItem = new MenuItem(updatedDetails, command.category());
            
            // Guardamos el nuevo item que tendrá el mismo ID debido a las reglas de JPA
            return menuItemRepository.save(updatedItem);
        });
    }

    @Override
    @Transactional
    public Optional<MenuItem> handle(DeleteMenuItemCommand command) {
        return menuItemRepository.findById(command.id()).map(item -> {
            menuItemRepository.delete(item);
            return item;
        });
    }
} 