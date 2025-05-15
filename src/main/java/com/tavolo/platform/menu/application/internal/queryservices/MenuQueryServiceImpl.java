package com.tavolo.platform.menu.application.internal.queryservices;

import com.tavolo.platform.menu.domain.model.aggregates.MenuItem;
import com.tavolo.platform.menu.domain.model.queries.GetAllMenuItemsQuery;
import com.tavolo.platform.menu.domain.model.queries.GetMenuItemByIdQuery;
import com.tavolo.platform.menu.domain.model.queries.GetMenuItemsByCategoryQuery;
import com.tavolo.platform.menu.domain.services.MenuQueryService;
import com.tavolo.platform.menu.infrastructure.persistence.jpa.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de consultas para el menú.
 */
@Service
public class MenuQueryServiceImpl implements MenuQueryService {
    private final MenuItemRepository menuItemRepository;

    public MenuQueryServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> handle(GetAllMenuItemsQuery query) {
        return menuItemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuItem> handle(GetMenuItemByIdQuery query) {
        return menuItemRepository.findById(query.id());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> handle(GetMenuItemsByCategoryQuery query) {
        return menuItemRepository.findAllByCategory(query.category());
    }
}
