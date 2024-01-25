package com.challnege.delivery.domain.review.dto;

import com.challnege.delivery.domain.member.entity.Member;
import com.challnege.delivery.domain.restaurant.entity.Restaurant;
import com.challnege.delivery.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private Member member;
    private Restaurant restaurant;
    private String nickname;
    private String content;
    private int rating;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.member = review.getMember();
        this.restaurant = review.getRestaurant();
        this.nickname = review.getNickname();
        this.content = review.getContent();
        this.rating = review.getRating();
    }
}
