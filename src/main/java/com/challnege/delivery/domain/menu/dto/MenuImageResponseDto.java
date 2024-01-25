package com.challnege.delivery.domain.menu.dto;

import com.challnege.delivery.domain.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class MenuImageResponseDto {
    private Long id;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MenuImageResponseDto(Menu menu) {
        this.id = menu.getId();
        this.imageUrl = menu.getImageUrl();
        this.createdAt = menu.getCreatedAt();
        this.modifiedAt = menu.getModifiedAt();
    }
}
