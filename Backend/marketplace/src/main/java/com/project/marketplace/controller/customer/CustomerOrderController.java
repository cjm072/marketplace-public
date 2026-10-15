package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.OrderRequestDto;
import com.project.marketplace.dto.OrderResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.pojo.OrderStatus;
import com.project.marketplace.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/customer/order")
public class CustomerOrderController {

    private final OrderService orderService;

    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<OrderResponseDto>> getAllOrdersByCustomer(@PathVariable("customerId") Integer customerId){
        List<OrderResponseDto> orders = orderService.getAllOrdersByCustomer(customerId);
        return ApiResponse.success(orders);
    }

    @GetMapping("/seller/{sellerId}")
    public ApiResponse<List<OrderResponseDto>> getAllOrdersBySeller(@PathVariable("sellerId") Integer sellerId){
        List<OrderResponseDto> orders = orderService.getAllOrdersBySeller(sellerId);
        return ApiResponse.success(orders);
    }

    @PostMapping
    public ApiResponse<OrderResponseDto> addOrder(@RequestBody OrderRequestDto dto){
        OrderResponseDto newOrder = orderService.addOrder(dto);
        return ApiResponse.success(newOrder);
    }

    @PatchMapping("{orderId}/status/{status}")
    public ApiResponse<OrderResponseDto> updateOrderStatus(@PathVariable("status") OrderStatus status, @PathVariable("orderId") Integer orderId){
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(status, orderId);
        return ApiResponse.success(updatedOrder);
    }
}
