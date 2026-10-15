package com.project.marketplace.dto;

import com.project.marketplace.pojo.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class CustomerResponseDto extends UserResponseDto {

    public CustomerResponseDto(Integer id, String username, String email, String phone, String image, Address address) {
        super(id, username, email, phone, image, address);
    }
}
