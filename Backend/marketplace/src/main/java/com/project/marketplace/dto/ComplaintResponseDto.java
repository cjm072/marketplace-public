package com.project.marketplace.dto;

public record ComplaintResponseDto(

        Integer id,

        String issue,

        Integer orderItemId,

        Integer customerId,

        Integer sellerId
) {
}
