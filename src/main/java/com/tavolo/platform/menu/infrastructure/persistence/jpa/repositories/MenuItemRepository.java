package com.tavolo.platform.menu.infrastructure.persistence.jpa.repositories;

import com.tavolo.platform.menu.domain.model.aggregates.MenuItem;
import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para acceder a los ítems del menú.
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    /**
     * Encuentra todos los ítems del menú que pertenecen a una categoría específica.
     * @param category Categoría para filtrar
     * @return Lista de ítems que pertenecen a la categoría
     */
    List<MenuItem> findAllByCategory(MenuItemCategory category);
} 