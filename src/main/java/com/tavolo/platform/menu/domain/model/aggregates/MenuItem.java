package com.tavolo.platform.menu.domain.model.aggregates;

import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;
import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemDetails;
import com.tavolo.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * Entidad principal que representa un ítem del menú.
 */
@Getter
@Entity
@Table(name = "menu_items")
public class MenuItem extends AuditableAbstractAggregateRoot<MenuItem> {

    @Embedded
    private MenuItemDetails details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuItemCategory category;

    protected MenuItem() {
        // Constructor para JPA
    }

    public MenuItem(MenuItemDetails details, MenuItemCategory category) {
        this.details = details;
        this.category = category;
    }

    public String getName() {
        return details.name();
    }

    public String getDescription() {
        return details.description();
    }

    public java.math.BigDecimal getPrice() {
        return details.price();
    }

    public String getImageBase64() {
        return details.imageBase64();
    }
} 