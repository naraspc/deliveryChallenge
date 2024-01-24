package com.challnege.delivery.domain.restaurant.controller;

import com.challnege.delivery.domain.restaurant.dto.RestaurantRequestDto;
import com.challnege.delivery.domain.restaurant.dto.RestaurantResponseDto;
import com.challnege.delivery.domain.restaurant.dto.RestaurantSearchResponseDto;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.restaurant.service.RestaurantService;
import com.challnege.delivery.global.audit.Category;
import com.challnege.delivery.global.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{restaurantId}/order")
    public String showOrderPage(@PathVariable Long restaurantId, Model model) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.findRestaurantById(restaurantId);
        model.addAttribute("restaurant", restaurantResponseDto);
        return "test"; // order.html 페이지로 이동
    }


    //        @PostMapping
//        public ResponseEntity<String> createRestaurant(@ModelAttribute RestaurantRequestDto restaurantRequestDto,
//                                                       @RequestParam long memberId) {
//            restaurantService.createRestaurant(restaurantRequestDto, memberId);
//            return new ResponseEntity<>("Restaurant created successfully", HttpStatus.OK);
//        }
//
    @PostMapping
    public String createRestaurant(@ModelAttribute RestaurantRequestDto restaurantRequestDto,
                                   @AuthenticationPrincipal UserDetails member) {
        restaurantService.createRestaurant(restaurantRequestDto, member);
        return "restaurantList";
    }

    @GetMapping("/{id}")
    public String findRestaurantById(@PathVariable Long id, Model model) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.findRestaurantById(id);
        model.addAttribute("restaurant", restaurantResponseDto);
        return "restaurantDetail";
    }

//        @GetMapping
//        public String findRestaurantByAll(Model model) {
//            List<RestaurantResponseDto> restaurants = restaurantService.findRestaurantByAll();
//            model.addAttribute("restaurants", restaurants);
//            return "restaurantList";
//        }

    @GetMapping
    public String pageFindRestaurantByAll(Model model,
                                          @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Restaurant> restaurantPage = restaurantService.pageFindRestaurantByAll(pageable);
        List<Restaurant> restaurantList = restaurantPage.getContent();

        PageDto pageDto = new PageDto<>(RestaurantResponseDto.fromListRestaurantEntity(restaurantList), restaurantPage);
        model.addAttribute("pageDto", pageDto);
        return "restaurantPage";
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        if (restaurantService.deleteRestaurant(id)) {
            return new ResponseEntity<>("Restaurant deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot delete restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/search")
//    public String findRestaurantsByCategoryAndName(@RequestParam("category") Category category,
//                                                   @RequestParam("name") String name,
//                                                   Model model) {
//        List<RestaurantResponseDto> restaurants = restaurantService.findRestaurantsByCategoryAndName(category, name);
//        model.addAttribute("restaurants", restaurants);
//        return "restaurantList";
//    }
    @GetMapping("/search")
    public String searchRestaurants(@RequestParam(name = "restaurantName", required = false) String restaurantName,
                                    @RequestParam(name = "address", required = false) String address,
                                    @RequestParam(name = "category", required = false) String category,
                                    @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC,page = 1) Pageable pageable,
                                    Model model) {
        Page<Restaurant> restaurantPage = restaurantService.searchOptions(pageable, restaurantName, address, category);
        List<Restaurant> restaurantList = restaurantPage.getContent();

        PageDto pageDto = new PageDto<>(RestaurantSearchResponseDto.fromListRestaurantEntity(restaurantList), restaurantPage);
        model.addAttribute("pageDto", pageDto);
        return "restaurantPage";
    }
}