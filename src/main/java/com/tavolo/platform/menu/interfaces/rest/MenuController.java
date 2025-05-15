package com.tavolo.platform.menu.interfaces.rest;

import com.tavolo.platform.menu.domain.model.commands.DeleteMenuItemCommand;
import com.tavolo.platform.menu.domain.model.queries.GetAllMenuItemsQuery;
import com.tavolo.platform.menu.domain.model.queries.GetMenuItemByIdQuery;
import com.tavolo.platform.menu.domain.model.queries.GetMenuItemsByCategoryQuery;
import com.tavolo.platform.menu.domain.model.valueobjects.MenuItemCategory;
import com.tavolo.platform.menu.domain.services.MenuCommandService;
import com.tavolo.platform.menu.domain.services.MenuQueryService;
import com.tavolo.platform.menu.interfaces.rest.resources.CreateMenuItemResource;
import com.tavolo.platform.menu.interfaces.rest.resources.MenuItemResource;
import com.tavolo.platform.menu.interfaces.rest.resources.UpdateMenuItemResource;
import com.tavolo.platform.menu.interfaces.rest.transform.CreateMenuItemCommandFromResourceAssembler;
import com.tavolo.platform.menu.interfaces.rest.transform.MenuItemResourceFromEntityAssembler;
import com.tavolo.platform.menu.interfaces.rest.transform.UpdateMenuItemCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/menu", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu", description = "Menu Management Endpoints")
public class MenuController {
    private final MenuCommandService menuCommandService;
    private final MenuQueryService menuQueryService;

    public MenuController(MenuCommandService menuCommandService, MenuQueryService menuQueryService) {
        this.menuCommandService = menuCommandService;
        this.menuQueryService = menuQueryService;
    }

    /**
     * Endpoint público para obtener todos los ítems del menú.
     * @return Lista de recursos MenuItemResource
     */
    @GetMapping
    public ResponseEntity<List<MenuItemResource>> getAllMenuItems() {
        var query = new GetAllMenuItemsQuery();
        var menuItems = menuQueryService.handle(query);
        var resources = menuItems.stream()
                .map(MenuItemResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    /**
     * Endpoint público para obtener un ítem del menú por su ID.
     * @param itemId ID del ítem
     * @return Recurso MenuItemResource o 404 si no se encuentra
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItemResource> getMenuItemById(@PathVariable Long itemId) {
        var query = new GetMenuItemByIdQuery(itemId);
        var menuItem = menuQueryService.handle(query);
        return menuItem.map(item -> ResponseEntity.ok(
                MenuItemResourceFromEntityAssembler.toResourceFromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint público para obtener ítems del menú por categoría.
     * @param category Categoría para filtrar
     * @return Lista de recursos MenuItemResource
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemResource>> getMenuItemsByCategory(@PathVariable String category) {
        try {
            MenuItemCategory menuItemCategory = MenuItemCategory.valueOf(category);
            var query = new GetMenuItemsByCategoryQuery(menuItemCategory);
            var menuItems = menuQueryService.handle(query);
            var resources = menuItems.stream()
                    .map(MenuItemResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint protegido para crear un nuevo ítem del menú.
     * @param resource Datos del nuevo ítem
     * @return Recurso MenuItemResource creado o 400 si hay un error
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResource> createMenuItem(@Valid @RequestBody CreateMenuItemResource resource) {
        var command = CreateMenuItemCommandFromResourceAssembler.toCommandFromResource(resource);
        var menuItem = menuCommandService.handle(command);
        return menuItem.map(item -> new ResponseEntity<>(
                MenuItemResourceFromEntityAssembler.toResourceFromEntity(item), HttpStatus.CREATED))
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Endpoint protegido para actualizar un ítem del menú existente.
     * @param itemId ID del ítem a actualizar
     * @param resource Datos actualizados
     * @return Recurso MenuItemResource actualizado o 404 si no se encuentra
     */
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResource> updateMenuItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateMenuItemResource resource) {
        var command = UpdateMenuItemCommandFromResourceAssembler.toCommandFromResource(itemId, resource);
        var menuItem = menuCommandService.handle(command);
        return menuItem.map(item -> ResponseEntity.ok(
                MenuItemResourceFromEntityAssembler.toResourceFromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint protegido para eliminar un ítem del menú.
     * @param itemId ID del ítem a eliminar
     * @return 204 No Content si se elimina correctamente o 404 si no se encuentra
     */
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long itemId) {
        var command = new DeleteMenuItemCommand(itemId);
        var menuItem = menuCommandService.handle(command);
        return menuItem.map(item -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
