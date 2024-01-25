package com.challnege.delivery.domain.order.repository;

import com.challnege.delivery.domain.order.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM orders o WHERE o.member.memberId = :memberId AND o.status = 'BEFORE_ORDER'")
    Optional<Order> findBeforeOrderById(@Param("memberId") Long memberId);
    Optional<Order> findByMember_MemberId(long memberId);

    @Query("SELECT o FROM orders o WHERE o.restaurant.Id = :restaurantId AND o.status = 'ON_DELIVERY'")
    List<Order> findOnDeliveryOrdersByRestaurantId(@Param("restaurantId") Long restaurantId);

}