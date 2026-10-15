package com.project.marketplace.service;

import com.project.marketplace.dto.ReviewRequestDto;
import com.project.marketplace.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto getReviewById(Integer reviewId);
    List<ReviewResponseDto> getAllReviewsByProduct(Integer productId);
    ReviewResponseDto addReview(ReviewRequestDto dto);
    ReviewResponseDto updateReview(ReviewRequestDto dto, Integer reviewId);
    void deleteReview(Integer reviewId);
}
