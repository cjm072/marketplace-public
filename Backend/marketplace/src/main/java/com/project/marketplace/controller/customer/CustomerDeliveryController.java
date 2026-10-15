package com.project.marketplace.controller.customer;

import com.project.marketplace.dto.DeliveryRequestDto;
import com.project.marketplace.dto.DeliveryResponseDto;
import com.project.marketplace.payload.ApiResponse;
import com.project.marketplace.service.DeliveryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/delivery")
public class CustomerDeliveryController {

    private final DeliveryService deliveryService;

    public CustomerDeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/{deliveryId}")
    public ApiResponse<DeliveryResponseDto> getDeliveryById(@PathVariable("deliveryId") Integer deliveryId) {
        DeliveryResponseDto delivery = deliveryService.getDeliveryById(deliveryId);
        return ApiResponse.success(delivery);
    }

    @PostMapping
    public ApiResponse<DeliveryResponseDto> addDelivery(@RequestBody DeliveryRequestDto dto) {
        DeliveryResponseDto newDelivery = deliveryService.addDelivery(dto);
        return ApiResponse.success(newDelivery);
    }
}
