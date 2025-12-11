package com.pepperjacks.foodplatform.service;

import com.pepperjacks.foodplatform.dto.menu.MenuItemDto;

import java.util.List;

public interface MenuService {
    
    List<MenuItemDto> getKitchenMenu(Long kitchenId);
    
    MenuItemDto getMenuItem(Long itemId);
}

