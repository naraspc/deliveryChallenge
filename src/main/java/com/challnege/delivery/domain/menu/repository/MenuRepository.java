package com.challnege.delivery.domain.menu.repository;


import com.challnege.delivery.domain.menu.entity.Menu;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByRestaurant(Restaurant restaurant);
}
