package com.project.marketplace.dto;

import com.project.marketplace.pojo.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class CustomerRequestDto extends UserRequestDto{
    public CustomerRequestDto(String username, String password, String email, String phone, UserRole role) {
        super(username, password, email, phone, role);
    }
}
