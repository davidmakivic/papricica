package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.Roles;

public record UserCreateDto(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        Roles role
) {
}
