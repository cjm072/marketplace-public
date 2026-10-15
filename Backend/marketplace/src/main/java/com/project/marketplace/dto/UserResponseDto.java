package com.project.marketplace.dto;

import com.project.marketplace.pojo.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;

    private String username;

    private String email;

    private String phone;

    private String image;

    private Address address;
}
