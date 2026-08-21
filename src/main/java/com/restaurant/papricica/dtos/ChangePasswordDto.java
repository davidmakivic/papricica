package com.restaurant.papricica.dtos;

public record ChangePasswordDto(
        String oldPassword,
        String newPassword
) {
}
