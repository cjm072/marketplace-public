package com.project.marketplace.service;

import com.project.marketplace.dto.ReviewRequestDto;
import com.project.marketplace.dto.ReviewResponseDto;
import com.project.marketplace.mapper.ReviewMapper;
import com.project.marketplace.pojo.Product;
import com.project.marketplace.pojo.Review;
import com.project.marketplace.repository.ProductRepository;
import com.project.marketplace.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.productRepository = productRepository;
    }

    @Override
    public ReviewResponseDto getReviewById(Integer reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " not found"));
        return reviewMapper.toReviewResponseDto(review);
    }

    @Override
    public List<ReviewResponseDto> getAllReviewsByProduct(Integer productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
        List<Review> reviews = reviewRepository.findAllByProduct(product);
        return reviewMapper.toReviewResponseDtoList(reviews);
    }

    @Override
    public ReviewResponseDto addReview(ReviewRequestDto dto){
        Review review = reviewMapper.toReview(dto);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewResponseDto(savedReview);
    }

    @Override
    public ReviewResponseDto updateReview(ReviewRequestDto dto, Integer reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review with ID " + reviewId + " not found"));
        reviewMapper.updateReviewFromDto(dto, review);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toReviewResponseDto(savedReview);
    }

    @Override
    public void deleteReview(Integer reviewId){
        reviewRepository.deleteById(reviewId);
    }
}
