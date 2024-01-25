package com.challnege.delivery.domain.restaurant.dto;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.global.audit.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public class RestaurantRequestDto { //valid 추가

    private final String restaurantName;
    private final String address;
    private final Category category;
    private final String resNumber;


}
