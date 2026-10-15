package com.project.marketplace.service;

import com.project.marketplace.dto.DeliveryRequestDto;
import com.project.marketplace.dto.DeliveryResponseDto;

public interface DeliveryService {
    DeliveryResponseDto getDeliveryById(Integer deliveryId);
    DeliveryResponseDto addDelivery(DeliveryRequestDto dto);
}
