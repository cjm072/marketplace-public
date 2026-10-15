package com.project.marketplace.mapper;

import com.project.marketplace.dto.PaymentRequestDto;
import com.project.marketplace.dto.PaymentResponseDto;
import com.project.marketplace.pojo.Payment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentMapper {
    public Payment toPayment(PaymentRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Payment payment = new Payment();

        updatePaymentFromDto(dto, payment);

        return  payment;
    }

    public void updatePaymentFromDto(PaymentRequestDto dto, Payment payment){
        payment.setMethod(dto.method());
        payment.setAmount(dto.amount());
        payment.setPaymentDate(dto.paymentDate());
    }

    public PaymentResponseDto toPaymentResponseDto(Payment payment){
        if ( payment == null ) {
            return null;
        }

        return new PaymentResponseDto(
                payment.getId(),
                payment.getMethod(),
                payment.getAmount(),
                payment.getPaymentDate()
        );
    }

    public List<PaymentResponseDto> toPaymentResponseDtoList(List<Payment> payments){
        if ( payments == null ) {
            return null;
        }
        List<PaymentResponseDto> list = new ArrayList<>( payments.size() );
        for ( Payment payment : payments ) {
            list.add( toPaymentResponseDto( payment ));
        }

        return  list;
    }
}
