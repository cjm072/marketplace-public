package com.project.marketplace.service;

import com.project.marketplace.dto.OrderItemResponseDto;
import com.project.marketplace.dto.OrderRequestDto;
import com.project.marketplace.dto.OrderResponseDto;
import com.project.marketplace.mapper.OrderMapper;
import com.project.marketplace.pojo.*;
import com.project.marketplace.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final OrderItemService orderItemService;
    private final CartItemRepository cartItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryService deliveryService;
    private final PaymentService paymentService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, CustomerRepository customerRepository, SellerRepository sellerRepository, OrderItemService orderItemService, CartItemRepository cartItemRepository, DeliveryRepository deliveryRepository, PaymentRepository paymentRepository, DeliveryService deliveryService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.orderItemService = orderItemService;
        this.cartItemRepository = cartItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository;
        this.deliveryService = deliveryService;
        this.paymentService = paymentService;
    }

    @Override
    public List<OrderResponseDto> getAllOrdersByCustomer(Integer customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer with ID " + customerId + " not found"));
        List<Order> orders = orderRepository.findAllByCustomer(customer);
        return orderMapper.toOrderResponseDtoList(orders);
    }

    @Override
    public List<OrderResponseDto> getAllOrdersBySeller(Integer sellerId){
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Seller with ID " + sellerId + " not found"));
        List<Order> orders = orderRepository.findAllBySeller(seller);
        return orderMapper.toOrderResponseDtoList(orders);
    }

    @Transactional
    @Override
    public OrderResponseDto addOrder(OrderRequestDto dto){
        Order order = orderMapper.toOrder(dto);
        order.setOrderStatus(OrderStatus.Completed);
        if (order.getVoucher() != null) {
            order.getVoucher().setQuantity(order.getVoucher().getQuantity() - 1);
            LocalDate today = LocalDate.now();
            if (today.isBefore(order.getVoucher().getStartDate()) || today.isAfter(order.getVoucher().getExpiryDate())) {
                throw new IllegalStateException("Voucher is not valid on this date.");
            }
            BigDecimal voucherAmount = order.getVoucher().getAmount();
            BigDecimal originalAmount = order.getPayment().getAmount();
            BigDecimal finalAmount = originalAmount.subtract(voucherAmount);
            if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
                finalAmount = BigDecimal.ZERO;
            }
            if (order.isCo2()) {
                finalAmount = finalAmount.subtract(BigDecimal.valueOf(0.1));
            }
            order.getPayment().setAmount(finalAmount);
        }
        Seller seller = order.getCartItems().get(0).getProduct().getSeller();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : order.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

        }
        List<OrderItem> savedOrderItems = orderItemService.addOrderItems(orderItems);
        Delivery delivery = order.getDelivery();
        delivery.setOrder(order);
        //deliveryRepository.save(delivery);
        Payment payment = order.getPayment();
        payment.setOrder(order);
        //paymentRepository.save(payment);
        order.setOrderItems(savedOrderItems);
        order.setSeller(seller);
        List<CartItem> cartItems = order.getCartItems();
        for (CartItem cartItem : cartItems) {
            cartItem.setOrder(order);
        }
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrderStatus(OrderStatus status, Integer orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order with ID " + orderId + " not found"));
        order.setOrderStatus(status);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDto(savedOrder);
    }
}
