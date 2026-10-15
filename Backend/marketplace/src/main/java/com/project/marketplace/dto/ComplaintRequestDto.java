package com.project.marketplace.dto;

public record ComplaintRequestDto(

        String issue,

        Integer orderItemId,

        Integer customerId,

        Integer sellerId
) {
}
