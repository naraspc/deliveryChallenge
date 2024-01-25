package com.challnege.delivery.domain.member.dto;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.member.entity.Role;
import com.challnege.delivery.domain.order.entity.Order;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberResponseDto {

    private long memberId;
    private String email;
    private String nickName;
    private String phoneNumber;
    private String address;
    private Role role;
    private List<Order> orderList;

    @Builder
    public MemberResponseDto(long memberId, String email, String nickName, String phoneNumber, String address, List<Order> orderList, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orderList = orderList;
        this.role = role;
    }

    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .orderList(member.getOrderList())
                .role(member.getRole())
                .build();
    }
}
