package com.challnege.delivery.domain.review.controller;

import com.challnege.delivery.domain.review.dto.ReviewResponseDto;
import com.challnege.delivery.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;

    @GetMapping("{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponseDto>> readAllReview(@PathVariable Long restaurantId){
        List<ReviewResponseDto> reviewResponseDtoList = reviewService.readAllReview(restaurantId);
        return ResponseEntity.ok(reviewResponseDtoList);
    }
}
