package com.challnege.delivery.domain.restaurant.dto;

import com.challnege.delivery.domain.menu.entity.Menu;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.global.audit.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Builder
public class RestaurantResponseDto {
    private final Long id;
    private final String restaurantName;
    private final String address;
    private final Category category;
    private final String resNumber;
    private final List<Menu> menu;
    private final int salesOfMonth;

    public static RestaurantResponseDto fromRestaurantEntity(Restaurant restaurant) {
        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .category(restaurant.getCategory())
                .resNumber(restaurant.getResNumber())
                .menu(restaurant.getMenu())
                .salesOfMonth(restaurant.getSalesOfMonth())
                .build();
    }

    public static List<RestaurantResponseDto> fromListRestaurantEntity(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantResponseDto::fromRestaurantEntity)
                .collect(Collectors.toList());
    }
}
