package com.pepperjacks.foodplatform.service.impl;

import com.pepperjacks.foodplatform.dto.menu.MenuItemDto;
import com.pepperjacks.foodplatform.model.KitchenEntity;
import com.pepperjacks.foodplatform.model.MenuItemEntity;
import com.pepperjacks.foodplatform.repository.KitchenRepository;
import com.pepperjacks.foodplatform.repository.MenuItemRepository;
import com.pepperjacks.foodplatform.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    
    private final MenuItemRepository menuItemRepository;
    private final KitchenRepository kitchenRepository;
    
    @Override
    public List<MenuItemDto> getKitchenMenu(Long kitchenId) {
        KitchenEntity kitchen = kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));
        
        List<MenuItemEntity> menuItems = menuItemRepository.findByKitchenIdAndIsAvailableTrue(kitchenId);
        
        return menuItems.stream()
                .map(this::mapToMenuItemDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public MenuItemDto getMenuItem(Long itemId) {
        MenuItemEntity menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        
        return mapToMenuItemDto(menuItem);
    }
    
    private MenuItemDto mapToMenuItemDto(MenuItemEntity item) {
        return MenuItemDto.builder()
                .id(item.getId())
                .kitchenId(item.getKitchenId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .category(item.getCategory())
                .imageUrl(item.getImageUrl())
                .isVeg(item.getIsVeg())
                .isAvailable(item.getIsAvailable())
                .prepTimeMinutes(item.getPrepTimeMinutes())
                .build();
    }
}
