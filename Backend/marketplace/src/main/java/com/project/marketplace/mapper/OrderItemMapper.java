package com.project.marketplace.mapper;

import com.project.marketplace.dto.OrderItemRequestDto;
import com.project.marketplace.dto.OrderItemResponseDto;
import com.project.marketplace.dto.OrderRequestDto;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.OrderRepository;
import com.project.marketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderItemMapper {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemMapper(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderItem toOrderItem(OrderItemRequestDto dto) {
        if (dto == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        updateOrderFromDto(dto, orderItem);
        return orderItem;
    }

    public void updateOrderFromDto(OrderItemRequestDto dto, OrderItem orderItem) {
        Product product = productRepository.findById(dto.productId())
                        .orElseThrow(() -> new NoSuchElementException("Product with ID " + dto.productId() + " not found"));
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.quantity());
        orderItem.setUnitPrice(dto.unitPrice());
    }

    public OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem) {
        if ( orderItem == null) {
            return null;
        }

        return new OrderItemResponseDto(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getImage(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getSubtotal(),
                orderItem.getProduct().getSeller().getId()
        );
    }

    public List<OrderItemResponseDto> toOrderItemResponseDtoList(List<OrderItem> orderItems) {
        if ( orderItems == null ) {
            return  null;
        }
        List<OrderItemResponseDto> list = new ArrayList<>( orderItems.size() );
        for ( OrderItem orderItem : orderItems ) {
            list.add( toOrderItemResponseDto( orderItem ));
        }

        return list;
    }
}
