package com.challnege.delivery.domain.review.repository;

import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByRestaurantAndReviewId(Restaurant restaurant, Long reviewId);

    List<Review> findAllByRestaurant(Restaurant restaurant);
}
