package com.project.marketplace.service;

import com.project.marketplace.dto.DeliveryRequestDto;
import com.project.marketplace.dto.DeliveryResponseDto;
import com.project.marketplace.mapper.DeliveryMapper;
import com.project.marketplace.pojo.Delivery;
import com.project.marketplace.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DeliveryServiceImpl implements DeliveryService{
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryMapper = deliveryMapper;
    }

    @Override
    public DeliveryResponseDto getDeliveryById(Integer deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoSuchElementException("Delivery with ID " + deliveryId + " not found"));
        return deliveryMapper.toDeliveryResponseDto(delivery);
    }

    @Override
    public DeliveryResponseDto addDelivery(DeliveryRequestDto dto) {
        Delivery delivery = deliveryMapper.toDelivery(dto);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDeliveryResponseDto(savedDelivery);
    }
}
