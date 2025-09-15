package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.Menu;
import io.github.devtamakuwala.dailydine.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Get all menus available in the database
     *
     */
    @GetMapping("")
    public List<Menu> getAllMenus() {
        return menuService.getAll();
    }

    @GetMapping("/active-menus")
    public List<Menu> getActiveMenus() {
        return menuService.getActiveMenus();
    }

    @GetMapping("/inactive-menus")
    public List<Menu> getInactiveMenus() {
        return menuService.getInactiveMenus();
    }

    @PostMapping("/mess/{messId}")
    public ResponseEntity<?> addMenu(@RequestBody Menu menu, @PathVariable int messId) {
        return menuService.addMenu(menu, messId);
    }

    @PutMapping("/mess/{messId}")
    public ResponseEntity<?> updateMenu(@RequestBody Menu menu, @PathVariable int messId) {
        return menuService.updateMenu(menu, messId);
    }

    @GetMapping("/mess/{messId}/date/{date}")
    public ResponseEntity<?> getMenuForMessByDate(@PathVariable String date, @PathVariable int messId) {
        return menuService.getMenuForMessByDate(date, messId);
    }
}
