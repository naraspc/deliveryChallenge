package com.challnege.delivery.domain.menu.entity;

import com.challnege.delivery.domain.menu.dto.MenuRequestDto;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.global.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Menu extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    // ERD 에서 수정해야할 것 같음. 레스토랑 1 : N 메뉴 로 바꿔야할듯.

    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    private int price;

//    @Lob
//    private byte[] image;

    public Menu(Restaurant restaurant, /*byte[] imageBytes,*/ MenuRequestDto menuRequestDto) {
        this.restaurant = restaurant;
        this.foodName = menuRequestDto.getFoodName();
        this.price = menuRequestDto.getPrice();
//        this.image = imageBytes;
    }

    public void updateMenu(Restaurant restaurant, /*byte[] imageBytes,*/ MenuRequestDto menuRequestDto) {
        this.restaurant = restaurant;
//        this.image = imageBytes;
        this.foodName = menuRequestDto.getFoodName();
        this.price = menuRequestDto.getPrice();
    }
}
