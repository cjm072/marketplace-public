package com.project.marketplace.dto;

import com.project.marketplace.pojo.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String username;

    private String password;

    private String email;

    private String phone;

    private UserRole role;
}
