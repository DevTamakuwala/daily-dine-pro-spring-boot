package io.github.devtamakuwala.dailydine.service;

import io.github.devtamakuwala.dailydine.model.Menu;
import io.github.devtamakuwala.dailydine.model.Mess;
import io.github.devtamakuwala.dailydine.repository.MenuRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MessService messService;

    public MenuService(MenuRepository menuRepository, MessService messService) {
        this.menuRepository = menuRepository;
        this.messService = messService;
    }

    /**
     * Retrieves all menus from the database and caches the result.
     * The result is cached in the "menus" cache with the key "'all'".
     */
    @Cacheable(value = "menus", key = "'all'")
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    /**
     * Retrieves all active menus and caches the result.
     * The result is cached in the "menus" cache with the key "'active'".
     */
    @Cacheable(value = "menus", key = "'active'")
    public List<Menu> getActiveMenus() {
        return menuRepository.findAllWhereExpiredIsFalse();
    }

    /**
     * Retrieves all inactive menus and caches the result.
     * The result is cached in the "menus" cache with the key "'inactive'".
     */
    @Cacheable(value = "menus", key = "'inactive'")
    public List<Menu> getInactiveMenus() {
        return menuRepository.findAllWhereExpiredIsTrue();
    }

    /**
     * Adds a new menu and evicts all entries from the "menus" cache.
     * This ensures that subsequent requests for menus will fetch fresh data.
     */
    @CacheEvict(value = "menus", allEntries = true)
    public ResponseEntity<?> addMenu(Menu menu, int messId) {

        if (menu != null) {
            Mess mess = (Mess) messService.getMessByMessId(messId).getBody();
            menu.setMess(mess);
            menu.setDate(menu.getDate());
            menuRepository.save(menu);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Updates an existing menu and evicts all entries from the "menus" cache.
     * This ensures that subsequent requests for menus will fetch fresh data.
     */
    @CacheEvict(value = "menus", allEntries = true)
    public ResponseEntity<?> updateMenu(Menu menu, int messId) {
        if (menu != null) {
            Mess mess = (Mess) messService.getMessByMessId(messId).getBody();
            menu.setMess(mess);
            menu.setAvailableFrom(menu.getAvailableFrom());
            menu.setAvailableTill(menu.getAvailableTill());
            menu.setDate(menu.getDate());
            menu.setDescription(menu.getDescription());
            menu.setExpired(menu.isExpired());
            menuRepository.save(menu);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<?> getMenuForMessByDate(String date, int messId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = simpleDateFormat.parse(date);
            Mess mess = (Mess) messService.getMessByMessId(messId).getBody();
            if (mess != null) {
                return ResponseEntity.ok(menuRepository.getMenuByDateAndMess(date1, mess));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.badRequest().build();
    }
}
