package com.challnege.delivery.domain.ordermenu.entity;

import com.challnege.delivery.domain.menu.entity.Menu;
import com.challnege.delivery.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderMenuId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long quantity;

    private Long totalPrice;

    @Builder
    public OrderMenu(Order order, Long quantity, Long totalPrice, Menu menu) {
        this.order = order;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.menu = menu;
    }
}