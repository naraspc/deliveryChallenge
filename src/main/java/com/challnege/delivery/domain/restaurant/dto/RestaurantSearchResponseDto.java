package com.challnege.delivery.domain.restaurant.dto;

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
public class RestaurantSearchResponseDto {

    private final Long id;
    private final String restaurantName;
    private final String address;
    private final Category category;
    private final String resNumber;

    public static RestaurantSearchResponseDto fromRestaurantEntity(Restaurant restaurant) {
        return RestaurantSearchResponseDto.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .category(restaurant.getCategory())
                .resNumber(restaurant.getResNumber())
                .build();
    }

    public static List<RestaurantSearchResponseDto> fromListRestaurantEntity(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantSearchResponseDto::fromRestaurantEntity)
                .collect(Collectors.toList());
    }
}
