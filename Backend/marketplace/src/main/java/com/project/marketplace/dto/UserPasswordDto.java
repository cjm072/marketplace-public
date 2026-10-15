package com.project.marketplace.dto;

public record UserPasswordDto(
        String newPassword,

        String confirmPassword
) {
}
