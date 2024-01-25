package com.challnege.delivery.domain.menu.dto;

import com.challnege.delivery.domain.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MenuResponseDto {
    private Long id;
    private String foodName;
    private int price;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MenuResponseDto(Menu createMenu) {
        this.id = createMenu.getId();
        this.foodName = createMenu.getFoodName();
        this.price = createMenu.getPrice();
        this.imageUrl = createMenu.getImageUrl();
        this.createdAt = createMenu.getCreatedAt();
        this.modifiedAt = createMenu.getModifiedAt();
    }
}
