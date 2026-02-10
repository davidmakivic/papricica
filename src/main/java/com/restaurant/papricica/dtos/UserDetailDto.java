package com.restaurant.papricica.dtos;

import com.restaurant.papricica.util.Roles;
import com.restaurant.papricica.util.UserStatus;

import java.time.OffsetDateTime;

public record UserDetailDto(
        Long userId,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Roles role,
        UserStatus status,
        OffsetDateTime createdAt
) {
}
