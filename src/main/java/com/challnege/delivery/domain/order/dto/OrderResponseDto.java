package com.challnege.delivery.domain.order.dto;

import com.challnege.delivery.domain.order.entity.Order;
import com.challnege.delivery.domain.order.entity.Status;
import com.challnege.delivery.domain.ordermenu.entity.OrderMenu;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {
    private Long orderId;

    private List<OrderMenu> orderMenuList;
    private Long totalPrice;
    private Status status;
    private String restaurantName;
    private String restaurantNumber;


    @Builder
    public OrderResponseDto(Long orderId,List<OrderMenu> orderMenuList, Long totalPrice, Status status, String restaurantName, String restaurantNumber) {
        this.orderId = orderId;
        this.orderMenuList = orderMenuList;
        this.totalPrice = totalPrice;
        this.status = status;
        this.restaurantName = restaurantName;
        this.restaurantNumber = restaurantNumber;
    }

    public static OrderResponseDto fromEntity(Order order, Restaurant restaurant) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderMenuList(order.getOrderMenuList())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .restaurantName(restaurant.getRestaurantName())
                .restaurantNumber(restaurant.getResNumber())
                .build();
    }

    public static OrderResponseDto fromEntityByOwner(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderMenuList(order.getOrderMenuList())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .build();
    }

    public static List<OrderResponseDto> fromEntityList(List<Order> orderList) {
        return orderList.stream()
                .map(OrderResponseDto::fromEntityByOwner)
                .collect(Collectors.toList());
    }
}
