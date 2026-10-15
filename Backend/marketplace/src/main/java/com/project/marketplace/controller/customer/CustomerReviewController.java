package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.ReviewRequestDto;
import com.project.marketplace.dto.ReviewResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customer/review")
public class CustomerReviewController {

    private final ReviewService reviewService;

    public CustomerReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ApiResponse<ReviewResponseDto> addReview(@RequestBody ReviewRequestDto dto){
        ReviewResponseDto newReview = reviewService.addReview(dto);
        return ApiResponse.success(newReview);
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<ReviewResponseDto> updateReview(@RequestBody ReviewRequestDto dto, @PathVariable("reviewId") Integer reviewId){
        ReviewResponseDto updatedReview = reviewService.updateReview(dto, reviewId);
        return  ApiResponse.success(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable("reviewId") Integer reviewId){
        reviewService.deleteReview(reviewId);
        return ApiResponse.success();
    }

}
