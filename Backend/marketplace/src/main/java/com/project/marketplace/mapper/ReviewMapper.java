package com.project.marketplace.mapper;

import com.project.marketplace.dto.ReviewRequestDto;
import com.project.marketplace.dto.ReviewResponseDto;
import com.project.marketplace.pojo.Customer;
import com.project.marketplace.pojo.Product;
import com.project.marketplace.pojo.Review;
import com.project.marketplace.repository.CustomerRepository;
import com.project.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewMapper {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public ReviewMapper(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public Review toReview(ReviewRequestDto dto){
        if ( dto == null ) {
            return null;
        }

        Product product = productRepository.findById(dto.product_id())
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + dto.product_id() + " not found"));
        System.out.println("Product is: " + product.getName());
        Customer customer = customerRepository.findById(dto.customer_id())
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + dto.customer_id() + " not found"));

        Review review = new Review();
        updateReviewFromDto(dto, review);
        review.setProduct(product);
        review.setCustomer(customer);

        return review;
    }

    public void updateReviewFromDto(ReviewRequestDto dto, Review review){
        review.setComment(dto.comment());
        review.setRating(dto.rating());
        review.setReviewDate(dto.reviewDate());
    }

    public ReviewResponseDto toReviewResponseDto(Review review){
        if ( review == null ) {
            return null;
        }

        return new ReviewResponseDto(
                review.getId(),
                review.getComment(),
                review.getRating(),
                review.getReviewDate(),
                review.getProduct().getId(),
                review.getCustomer().getId()
        );
    }

    public List<ReviewResponseDto> toReviewResponseDtoList(List<Review> reviews){
        if ( reviews == null ) {
            return null;
        }
        List<ReviewResponseDto> list = new ArrayList<>( reviews.size() );
        for ( Review review : reviews )
            list.add( toReviewResponseDto( review ) );

        return list;
    }
}
