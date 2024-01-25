package com.challnege.delivery.domain.order.service;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.member.service.MemberService;
import com.challnege.delivery.domain.menu.entity.Menu;
import com.challnege.delivery.domain.menu.repository.MenuRepository;
import com.challnege.delivery.domain.menu.service.MenuService;
import com.challnege.delivery.domain.order.dto.OrderResponseDto;
import com.challnege.delivery.domain.order.entity.Order;
import com.challnege.delivery.domain.order.entity.Status;
import com.challnege.delivery.domain.order.repository.OrderRepository;
import com.challnege.delivery.domain.ordermenu.entity.OrderMenu;
import com.challnege.delivery.domain.ordermenu.repository.OrderMenuRepository;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.restaurant.repository.RestaurantRepository;
import com.challnege.delivery.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final OrderMenuRepository orderMenuRepository;
    private final MenuService menuService;
    private final RestaurantService restaurantService;

    @Transactional(readOnly = true)
    public OrderResponseDto readCurrentOrder(UserDetails auth) {
        Member member = memberService.findMemberByEmail(auth.getUsername());
        Order order = findBeforeOrderById(member.getMemberId());
        return OrderResponseDto.fromEntityByOwner(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> readOrdersListByOwner(long restaurantId, UserDetails auth) {
        Member member = memberService.findMemberByEmail(auth.getUsername());
        List<Order> orderList = findAllOrderByRestaurantId(restaurantId);
        return OrderResponseDto.fromEntityList(orderList);
    }

    public OrderResponseDto addToOrder(long restaurantId, long menuId, long quantity, UserDetails auth) {
        Member member = memberService.findMemberByEmail(auth.getUsername());
        Menu menu = menuService.findMenuById(menuId);
        Order order = getOrderByMember(member);
        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantId);

        OrderMenu orderMenu = OrderMenu.builder()
                .order(order)
                .menu(menu)
                .quantity(quantity)
                .totalPrice(menu.getPrice() * quantity)
                .build();

        orderMenuRepository.save(orderMenu);
        order.updateRestaurant(restaurant);
        order.updateTotalPrice(menu.getPrice() * quantity);
        return OrderResponseDto.fromEntity(order, restaurant);
    }


    public OrderResponseDto makeOrder(long orderId, UserDetails auth) {
        Order order = findOrderByOrderId(orderId);
        Member member = memberService.findMemberByEmail(auth.getUsername());

        isValidUser(order, member.getMemberId());

        order.makeOnDelivery();

        return OrderResponseDto.fromEntityByOwner(order);
    }
    public OrderResponseDto makeOrderTest(UserDetails auth) {
        Member member = memberService.findMemberByEmail(auth.getUsername());
        Order order = findBeforeOrderById(member.getMemberId());
        order.makeOnDelivery();
        return OrderResponseDto.fromEntityByOwner(order);
    }

    public OrderResponseDto completeOrder(long restaurantId, long orderId, UserDetails auth) {
        Order order = findOrderByOrderId(orderId);
        Member member = memberService.findMemberByEmail(auth.getUsername());

        Restaurant restaurant = restaurantService.findRestaurantByResId(restaurantId);
        menuService.isValidOwner(restaurant, member.getMemberId());

        order.makeOnComplete();

        return OrderResponseDto.fromEntityByOwner(order);

    }

    private Order getOrderByMember(Member member) {
        Order order = orderRepository.findBeforeOrderById(member.getMemberId())
                .orElseGet(()-> {
                    Order newOrder = Order.builder()
                            .member(member)
                            .totalPrice(0L)
                            .status(Status.BEFORE_ORDER)
                            .build();
                    return orderRepository.save(newOrder);
                });
        return order;
    }

    @Transactional(readOnly = true)
    public Order findBeforeOrderById(long memberId) {
        Optional<Order> optionalOrder = orderRepository.findBeforeOrderById(memberId);
        Order order = optionalOrder.orElseThrow(() -> new NullPointerException("주문 사항이 없습니다"));
        return order;

    }

    @Transactional(readOnly = true)
    public Order findOrderByOrderId(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.orElseThrow(() -> new NullPointerException("주문 사항이 없습니다"));
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrderByRestaurantId(long restaurantId) {
        List<Order> orderList = orderRepository.findOnDeliveryOrdersByRestaurantId(restaurantId);
        return orderList;
    }

    @Transactional(readOnly = true)
    public void isValidUser(Order order, long memberId) {
        if (!order.getMember().getMemberId().equals(memberId)) {
            throw new NoSuchElementException("해당 유저만 주문이 가능합니다.");
        }
    }
}