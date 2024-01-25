package com.challnege.delivery.domain.review.controller;

import com.challnege.delivery.domain.review.dto.ReviewRequestDto;
import com.challnege.delivery.domain.review.dto.ReviewResponseDto;
import com.challnege.delivery.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
// Set a base path for the controller
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/restaurants/{restaurantId}/reviews")
    public String postReview(@PathVariable Long restaurantId,
                             @ModelAttribute ReviewRequestDto requestDto,
                             Model model) {
        System.out.println(requestDto.getMemberId());
        ReviewResponseDto responseDto = reviewService.postReview(restaurantId, requestDto);
        model.addAttribute("reviewResponseDto", responseDto);
        return "reviewResult"; // Return the view name
    }

    @DeleteMapping("/restaurants/{restaurantId}/reviews/{reviewId}")
    public String deleteReview(@PathVariable Long restaurantId,
                               @PathVariable Long reviewId,
                               Model model) {
        ReviewResponseDto responseDto = reviewService.deleteReview(restaurantId, reviewId);
        model.addAttribute("reviewResponseDto", responseDto);
        return "reviewResult"; // Return the view name
    }
}
