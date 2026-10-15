package com.project.marketplace.controller;

import com.project.marketplace.dto.ReviewResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewResponseDto> getReviewById(@PathVariable("reviewId") Integer reviewId){
        ReviewResponseDto review = reviewService.getReviewById(reviewId);
        return ApiResponse.success(review);
    }

    @GetMapping("/batch/{productId}")
    public ApiResponse<List<ReviewResponseDto>> getAllReviewsByProduct(@PathVariable("productId") Integer productId){
        List<ReviewResponseDto> reviews = reviewService.getAllReviewsByProduct(productId);
        return ApiResponse.success(reviews);
    }
}
