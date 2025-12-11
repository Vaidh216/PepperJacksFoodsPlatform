package com.pepperjacks.foodplatform.controller;

import com.pepperjacks.foodplatform.dto.menu.MenuItemDto;
import com.pepperjacks.foodplatform.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {
    
    private final MenuService menuService;
    
    @GetMapping("/kitchen/{kitchenId}")
    public ResponseEntity<List<MenuItemDto>> getKitchenMenu(@PathVariable Long kitchenId) {
        List<MenuItemDto> menu = menuService.getKitchenMenu(kitchenId);
        return ResponseEntity.ok(menu);
    }
    
    @GetMapping("/item/{itemId}")
    public ResponseEntity<MenuItemDto> getMenuItem(@PathVariable Long itemId) {
        MenuItemDto item = menuService.getMenuItem(itemId);
        return ResponseEntity.ok(item);
    }
}

