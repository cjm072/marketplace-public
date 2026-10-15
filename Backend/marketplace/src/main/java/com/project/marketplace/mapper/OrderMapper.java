package com.project.marketplace.mapper;

import com.project.marketplace.dto.*;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.CartItemRepository;
import com.project.marketplace.repository.CustomerRepository;
import com.project.marketplace.repository.OrderItemRepository;
import com.project.marketplace.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderMapper {

    private final DeliveryMapper deliveryMapper;
    private final PaymentMapper paymentMapper;
    private final VoucherMapper voucherMapper;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;
    private final VoucherRepository voucherRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderMapper(DeliveryMapper deliveryMapper, PaymentMapper paymentMapper, VoucherMapper voucherMapper, CustomerRepository customerRepository, CartItemRepository cartItemRepository, VoucherRepository voucherRepository, OrderItemMapper orderItemMapper) {
        this.deliveryMapper = deliveryMapper;
        this.paymentMapper = paymentMapper;
        this.voucherMapper = voucherMapper;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
        this.voucherRepository = voucherRepository;
        this.orderItemMapper = orderItemMapper;
    }

    public Order toOrder(OrderRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();

        updateOrderFromDto(dto, order);

        return order;
    }

    public void updateOrderFromDto(OrderRequestDto dto, Order order) {
        Delivery delivery = deliveryMapper.toDelivery(dto.delivery());
        Payment payment = paymentMapper.toPayment(dto.payment());
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + dto.customerId() + " not found"));
        if (dto.voucherId() != null) {
            Voucher voucher = voucherRepository.findById(dto.voucherId())
                    .orElseThrow(() -> new NoSuchElementException("Voucher with ID " + dto.voucherId() + " not found"));
            order.setVoucher(voucher);
        }
        List<CartItem> cartItems = cartItemRepository.findAllById(dto.cartItemIds());
        if (cartItems.size() != dto.cartItemIds().size()) {
            throw new NoSuchElementException("One or more order items not found for provided IDs: " + dto.cartItemIds());
        }
        order.setCo2(dto.co2());
        order.setDelivery(delivery);
        order.setPayment(payment);
        order.setCustomer(customer);
        order.setCartItems(cartItems);
    }

    public OrderResponseDto toOrderResponseDto(Order order){
        if ( order == null ) {
            return null;
        }

        DeliveryResponseDto deliveryResponseDto = deliveryMapper.toDeliveryResponseDto(order.getDelivery());
        PaymentResponseDto paymentResponseDto = paymentMapper.toPaymentResponseDto(order.getPayment());
        VoucherResponseDto voucherResponseDto = voucherMapper.toVoucherResponseDto(order.getVoucher());
        List<OrderItemResponseDto> orderItemResponseDtos = orderItemMapper.toOrderItemResponseDtoList(order.getOrderItems());

        return new OrderResponseDto(
                order.getId(),
                order.getOrderStatus(),
                order.isCo2(),
                deliveryResponseDto,
                paymentResponseDto,
                voucherResponseDto,
                order.getCustomer().getId(),
                orderItemResponseDtos
        );
    }

    public List<OrderResponseDto> toOrderResponseDtoList(List<Order> orders){
        if ( orders == null ) {
            return null;
        }
        List<OrderResponseDto> list = new ArrayList<>( orders.size() );
        for ( Order order : orders ) {
            list.add( toOrderResponseDto( order ));
        }

        return list;
    }
}
