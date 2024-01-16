package com.challnege.delivery.domain.restaurant.controller;

import com.challnege.delivery.domain.restaurant.dto.RestaurantRequestDto;
import com.challnege.delivery.domain.restaurant.dto.RestaurantResponseDto;
import com.challnege.delivery.domain.restaurant.service.RestaurantService;
import com.challnege.delivery.global.audit.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @Controller
    @RequestMapping("/restaurants")
    @RequiredArgsConstructor
    public class RestaurantController {

        private final RestaurantService restaurantService;

        @GetMapping("/form")
        public String showRestaurantForm() {
            return "restaurantForm";
        }


//        @PreAuthorize("hasRole('OWNER')")
        @PostMapping
        public ResponseEntity<String> createRestaurant(@ModelAttribute RestaurantRequestDto restaurantRequestDto,
                                                       @AuthenticationPrincipal UserDetails member) {
            restaurantService.createRestaurant(restaurantRequestDto, member);
            return new ResponseEntity<>("Restaurant created successfully", HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public String findRestaurantById(@PathVariable Long id, Model model) {
            RestaurantResponseDto restaurantResponseDto = restaurantService.findRestaurantById(id);
            model.addAttribute("restaurant", restaurantResponseDto);
            return "restaurantDetail";
        }

        @GetMapping
        public String findRestaurantByAll(Model model) {
            List<RestaurantResponseDto> restaurants = restaurantService.findRestaurantByAll();
            model.addAttribute("restaurants", restaurants);
            return "restaurantList";
        }

        @DeleteMapping("/{id}/delete")
        public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
            if (restaurantService.deleteRestaurant(id)) {
                return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot delete restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        @GetMapping("/search")
        public String findRestaurantsByCategoryAndName(@RequestParam("category") Category category,
                                                       @RequestParam("name") String name,
                                                       Model model) {
            List<RestaurantResponseDto> restaurants = restaurantService.findRestaurantsByCategoryAndName(category, name);
            model.addAttribute("restaurants", restaurants);
            return "restaurantList";
        }

    }