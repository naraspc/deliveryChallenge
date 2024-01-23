package com.challnege.delivery.domain.order.controller;

import com.challnege.delivery.domain.order.dto.OrderResponseDto;
import com.challnege.delivery.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants/{restaurantId}/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{menuId}")
    private ResponseEntity addToOrder(@PathVariable("restaurantId") long restaurantId,
                              @PathVariable("menuId") long menuId,
                              @RequestParam long quantity,
                              @AuthenticationPrincipal UserDetails auth) {
        OrderResponseDto orderResponseDto = orderService.addToOrder(restaurantId, menuId, quantity, auth);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public String readCurrentOrder(@PathVariable("restaurantId") long restaurantId,
                                   @AuthenticationPrincipal UserDetails auth,
                                   Model model) {
        OrderResponseDto orderResponseDto = orderService.readCurrentOrder(auth);
        model.addAttribute("orderResponseDto", orderResponseDto);
        return "orders";
    }

    @GetMapping("/owner")
    public String readOrdersByOwner(@PathVariable("restaurantId") long restaurantId,
                                    @AuthenticationPrincipal UserDetails auth,
                                    Model model) {
        List<OrderResponseDto> orderResponseDtoList = orderService.readOrdersListByOwner(restaurantId, auth);

        model.addAttribute("orderOfOwner", orderResponseDtoList);

        return "orderOfOwner";
    }
    @PatchMapping
    public String makeOrder(@PathVariable("restaurantId") long restaurantId,
                            @AuthenticationPrincipal UserDetails auth,
                            Model model) {
        OrderResponseDto orderResponseDto = orderService.makeOrderTest(auth);
        return "orders";
    }

    @PatchMapping("/owner/{orderId}")
    public String completeOrder(@PathVariable("restaurantId") long restaurantId,
                                @PathVariable("orderId") long orderId,
                                @AuthenticationPrincipal UserDetails auth,
                            Model model) {
        OrderResponseDto orderResponseDto = orderService.completeOrder(restaurantId,orderId, auth);
        model.addAttribute("orderResponseDto", orderResponseDto);
        return "index";
    }
}