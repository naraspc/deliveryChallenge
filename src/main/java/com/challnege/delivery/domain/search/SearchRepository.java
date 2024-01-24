package com.challnege.delivery.domain.search;

import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


public interface SearchRepository {

    Page<Restaurant> findBySearchOption(Pageable pageable, String restaurantName, String address, String category);
}
