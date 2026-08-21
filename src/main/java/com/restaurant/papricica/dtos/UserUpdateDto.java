package com.restaurant.papricica.dtos;

public record UserUpdateDto(
        String firstName,
        String lastName,
        String phoneNumber
) {
}
