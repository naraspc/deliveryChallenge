package com.challnege.delivery.domain.restaurant.service;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.member.service.MemberService;
import com.challnege.delivery.domain.restaurant.dto.RestaurantRequestDto;
import com.challnege.delivery.domain.restaurant.dto.RestaurantResponseDto;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.restaurant.repository.RestaurantRepository;
import com.challnege.delivery.domain.search.SearchRepository;
import com.challnege.delivery.domain.search.SearchRepositoryImpl;
import com.challnege.delivery.global.audit.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberService memberService;
    private final SearchRepository searchRepository;

    //Create
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto, UserDetails auth) {
        Member member = memberService.findMemberByEmail(auth.getUsername());

        Restaurant restaurant = Restaurant.builder()
                .member(member)
                .restaurantName(restaurantRequestDto.getRestaurantName())
                .category(restaurantRequestDto.getCategory())
                .address(restaurantRequestDto.getAddress())
                .resNumber(restaurantRequestDto.getResNumber())
                .build();

        restaurantRepository.save(restaurant);

        return RestaurantResponseDto.fromRestaurantEntity(restaurant);
    }


    //Read find by one
    public RestaurantResponseDto findRestaurantById(Long id) {
        Restaurant restaurant = findRestaurantByResId(id);

        return RestaurantResponseDto.fromRestaurantEntity(restaurant);
    }


    //Read find all
    public List<RestaurantResponseDto> findRestaurantByAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return RestaurantResponseDto.fromListRestaurantEntity(restaurants);

    }
    public Page<Restaurant> pageFindRestaurantByAll(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return restaurantRepository.findAll(pageRequest);
    }

    //Read by category and name
    @Transactional(readOnly = true)
    public Page<RestaurantResponseDto> searchRestaurantsPageable(String keyword, Pageable pageable) {
        List<Category> categories = findCategoriesByKeyword(keyword);

        if (!categories.isEmpty()) {
            return searchByCategoriesPageable(categories, pageable);
        } else {
            return searchByNamePageable(keyword, pageable);
        }
    }

    public List<Category> findCategoriesByKeyword(String keyword) {
        List<Category> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            if (category.name().contains(keyword)) {
                categories.add(category);
            }
        }
        return categories;
    }

    public Page<RestaurantResponseDto> searchByCategoriesPageable(List<Category> categories, Pageable pageable) {
        Page<Restaurant> findByCategories = restaurantRepository.findRestaurantsByCategoryIn(categories, pageable);
        return findByCategories.map(RestaurantResponseDto::fromRestaurantEntity);
    }

    public Page<RestaurantResponseDto> searchByNamePageable(String keyword, Pageable pageable) {
        Page<Restaurant> findByName = restaurantRepository.findRestaurantsByRestaurantNameContaining(keyword, pageable);
        return findByName.map(RestaurantResponseDto::fromRestaurantEntity);
    }
    //Delete
    public Boolean deleteRestaurant(long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);

        if (restaurant.isPresent()) {
            restaurantRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public Restaurant findRestaurantByResId(long restaurantId) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant = optionalRestaurant.orElseThrow(() -> new NullPointerException("식당을 찾을 수 없습니다."));
        return restaurant;
    }

    public Page<Restaurant> searchOptions(Pageable pageable, String restaurantName, String address, String category) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        return searchRepository.findBySearchOption(pageRequest, restaurantName, address, category);

    }
}
