package com.project.marketplace.mapper;

import com.project.marketplace.dto.DeliveryRequestDto;
import com.project.marketplace.dto.DeliveryResponseDto;
import com.project.marketplace.pojo.Delivery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryMapper {
    public Delivery toDelivery(DeliveryRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Delivery delivery = new Delivery();

        updateDeliveryFromDto(dto, delivery);

        return delivery;
    }

    public void updateDeliveryFromDto(DeliveryRequestDto dto, Delivery delivery){
        delivery.setMethod(dto.method());
        delivery.setCourier(dto.courier());
        delivery.setShippingAddress(dto.shippingAddress());
    }

    public DeliveryResponseDto toDeliveryResponseDto(Delivery delivery){
        if ( delivery == null ) {
            return null;
        }

        return new DeliveryResponseDto(
                delivery.getId(),
                delivery.getMethod(),
                delivery.getPrice(),
                delivery.getEmissionRate(),
                delivery.getCourier(),
                delivery.getDispatchedAt(),
                delivery.getDeliveredAt(),
                delivery.getShippingAddress()
        );
    }
    public List<DeliveryResponseDto> toDeliveryResponseDtoList(List<Delivery> deliveries){
        if ( deliveries == null ) {
            return null;
        }
        List<DeliveryResponseDto> list = new ArrayList<>( deliveries.size() );
        for ( Delivery delivery : deliveries ) {
            list.add( toDeliveryResponseDto( delivery ));
        }

        return  list;
    }
}
