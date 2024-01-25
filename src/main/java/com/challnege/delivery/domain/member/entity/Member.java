package com.challnege.delivery.domain.member.entity;

import com.challnege.delivery.domain.order.entity.Order;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.global.audit.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Restaurant> restaurantList = new ArrayList<>();

    @Builder
    public Member(List<Restaurant> restaurantList, String email, String password, String nickName, String phoneNumber, String address, Role role) {
        this.restaurantList = restaurantList;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
